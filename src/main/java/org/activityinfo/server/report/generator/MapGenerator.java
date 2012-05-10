/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.dao.IndicatorDAO;
import org.activityinfo.server.database.hibernate.entity.Indicator;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.generator.map.BubbleLayerGenerator;
import org.activityinfo.server.report.generator.map.IconLayerGenerator;
import org.activityinfo.server.report.generator.map.LayerGenerator;
import org.activityinfo.server.report.generator.map.Margins;
import org.activityinfo.server.report.generator.map.PiechartLayerGenerator;
import org.activityinfo.server.report.generator.map.TiledMap;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetBaseMaps;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.BaseMapResult;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.map.BaseMap;
import org.activityinfo.shared.map.GoogleBaseMap;
import org.activityinfo.shared.map.PredefinedBaseMaps;
import org.activityinfo.shared.map.TileBaseMap;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;
import org.activityinfo.shared.report.model.layers.IconMapLayer;
import org.activityinfo.shared.report.model.layers.MapLayer;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer;
import org.activityinfo.shared.util.mapping.Extents;
import org.activityinfo.shared.util.mapping.TileMath;
import org.apache.log4j.Logger;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 */
public class MapGenerator extends ListGenerator<MapReportElement> {

	private final IndicatorDAO indicatorDAO;

    private static final Logger logger = Logger.getLogger(MapGenerator.class);
        
    @Inject
    public MapGenerator(DispatcherSync dispatcher, IndicatorDAO indicatorDAO) {
        super(dispatcher);
        this.indicatorDAO = indicatorDAO;
    }

    @Override
	public void generate(User user, MapReportElement element, Filter inheritedFilter, DateRange dateRange) {

        Filter filter = GeneratorUtils.resolveElementFilter(element, dateRange);
        Filter effectiveFilter = inheritedFilter == null ? filter : new Filter(inheritedFilter, filter);


        MapContent content = new MapContent();
        content.setFilterDescriptions(generateFilterDescriptions(filter, Collections.<DimensionType>emptySet(), user));

        // Set up layer generators
        List<LayerGenerator> layerGenerators = new ArrayList<LayerGenerator>();
        for (MapLayer layer : element.getLayers()) {
        	if (layer.isVisible()) {
        		// Add indicator
        		Filter layerFilter = new Filter(effectiveFilter, layer.getFilter());
        		filter.addRestriction(DimensionType.Indicator, layer.getIndicatorIds());
                List<SiteDTO> sites = getDispatcher().execute(new GetSites(layerFilter)).getData();
                
	            if (layer instanceof BubbleMapLayer) {
	                layerGenerators.add(new BubbleLayerGenerator((BubbleMapLayer) layer, sites));
	            } else if (layer instanceof IconMapLayer) {
	                layerGenerators.add(new IconLayerGenerator((IconMapLayer) layer, sites));
	            } else if (layer instanceof PiechartMapLayer) {
	            	layerGenerators.add(new PiechartLayerGenerator((PiechartMapLayer) layer, sites));
	            }
        	}
        }

        // FIRST PASS: calculate extents and margins
        int width = element.getWidth();
        int height = element.getHeight();
        AiLatLng center;
        int zoom;
        
        if(element.getCenter() == null) {
	        Extents extents = Extents.emptyExtents();
	        Margins margins = new Margins(0);
	        for (LayerGenerator layerGtor : layerGenerators) {
	            extents.grow(layerGtor.calculateExtents());
	            margins.grow(layerGtor.calculateMargins());
	        }
	        // Now we're ready to calculate the zoom level
	        // and the projection
	        zoom = TileMath.zoomLevelForExtents(extents, width, height);
	        center = extents.center();
	        
        } else {
        	center = element.getCenter();
        	zoom = element.getZoomLevel();  	
        }
        
        content.setCenter(center);

        // Retrieve the basemap and clamp zoom level
        BaseMap baseMap = null;
        if (element.getBaseMapId() == null) {
        	// default
        	baseMap = GoogleBaseMap.ROADMAP;
        } else if(PredefinedBaseMaps.isPredefinedMap(element.getBaseMapId())) {
        	baseMap = PredefinedBaseMaps.forId(element.getBaseMapId());
        } else {
        	baseMap = getBaseMap(element.getBaseMapId());
        } 
                
        if (zoom < baseMap.getMinZoom()) {
            zoom = baseMap.getMinZoom();
        }
        if (zoom > baseMap.getMaxZoom()) {
            zoom = baseMap.getMaxZoom();
        }

        TiledMap map = new TiledMap(width, height, center, zoom);
        content.setBaseMap(baseMap);
        content.setZoomLevel(zoom);
        if (baseMap == null) {
        	baseMap = TileBaseMap.createNullMap(element.getBaseMapId());
			logger.error("Could not find base map id=" + element.getBaseMapId());
        }

        // Generate the actual content
        for (LayerGenerator layerGtor : layerGenerators) {
            layerGtor.generate(map, content);
        }
        
        // Get relevant indicators for the map markers
        Set<Integer> indicatorIds = new HashSet<Integer>(); 
        
        for (MapLayer maplayer : element.getLayers()) {
        	indicatorIds.addAll(maplayer.getIndicatorIds());
        }
        
        Set<IndicatorDTO> indicatorDTOs = new HashSet<IndicatorDTO>();
        for (Integer indicatorId : indicatorIds) {
        	Indicator indicator = indicatorDAO.findById(indicatorId);
        	if (indicator != null) {
	        	IndicatorDTO indicatorDTO = new IndicatorDTO();
	        	indicatorDTO.setId(indicator.getId());
	        	indicatorDTO.setName(indicator.getName());
	        	
	        	indicatorDTOs.add(indicatorDTO);
        	}
        }
        
        content.setIndicators(indicatorDTOs);
        element.setContent(content);

    }

	private BaseMap getBaseMap(String baseMapId) {
		BaseMapResult maps = dispatcher.execute(new GetBaseMaps());
		for(TileBaseMap map : maps.getBaseMaps()) {
			if(map.getId().equals(baseMapId)) {
				return map;
			}
		}
		logger.error("Could not find base map id=" +  baseMapId);
		
    	return TileBaseMap.createNullMap(baseMapId);
	}
}
