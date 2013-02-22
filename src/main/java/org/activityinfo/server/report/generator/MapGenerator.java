

package org.activityinfo.server.report.generator;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.dao.IndicatorDAO;
import org.activityinfo.server.database.hibernate.entity.Country;
import org.activityinfo.server.database.hibernate.entity.Indicator;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.generator.map.BubbleLayerGenerator;
import org.activityinfo.server.report.generator.map.IconLayerGenerator;
import org.activityinfo.server.report.generator.map.LayerGenerator;
import org.activityinfo.server.report.generator.map.Margins;
import org.activityinfo.server.report.generator.map.PiechartLayerGenerator;
import org.activityinfo.server.report.generator.map.PolygonLayerGenerator;
import org.activityinfo.server.report.generator.map.TiledMap;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetBaseMaps;
import org.activityinfo.shared.command.result.BaseMapResult;
import org.activityinfo.shared.dto.IndicatorDTO;
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
import org.activityinfo.shared.report.model.layers.PolygonMapLayer;
import org.activityinfo.shared.util.mapping.Extents;
import org.activityinfo.shared.util.mapping.TileMath;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

/**
 * @author Alex Bertram
 */
public class MapGenerator extends ListGenerator<MapReportElement> {

	private final IndicatorDAO indicatorDAO;

    private static final Logger logger = Logger.getLogger(MapGenerator.class.getName());
        
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
        		LayerGenerator layerGtor = createGenerator(layer);
	            layerGtor.query(getDispatcher(), effectiveFilter);
	            layerGenerators.add(layerGtor);
        	}
        }

        // FIRST PASS: calculate extents and margins
        int width = element.getWidth();
        int height = element.getHeight();
        AiLatLng center;
        int zoom;
        
        Extents extents = Extents.emptyExtents();
        Margins margins = new Margins(0);
        for (LayerGenerator layerGtor : layerGenerators) {
            extents.grow(layerGtor.calculateExtents());
            margins.grow(layerGtor.calculateMargins());
        }
        content.setExtents(extents);
        
        if(element.getCenter() == null) {

	        // Now we're ready to calculate the zoom level
	        // and the projection
	        zoom = TileMath.zoomLevelForExtents(extents, width, height);
	        center = extents.center();
	        
        } else {
        	center = element.getCenter();
        	zoom = element.getZoomLevel();  	
        }
        
        content.setCenter(center);

        List<Indicator> indicators = queryIndicators(element);
        
        
        // Retrieve the basemap and clamp zoom level
        BaseMap baseMap = findBaseMap(element, indicators); 
                
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
			logger.log(Level.SEVERE, "Could not find base map id=" + element.getBaseMapId());
        }

        // Generate the actual content
        for (LayerGenerator layerGtor : layerGenerators) {
            layerGtor.generate(map, content);
        }
                
        content.setIndicators(toDTOs(indicators));
        element.setContent(content);

    }

	private LayerGenerator createGenerator(MapLayer layer) {
		if (layer instanceof BubbleMapLayer) {
			return new BubbleLayerGenerator((BubbleMapLayer) layer);
		} else if (layer instanceof IconMapLayer) {
			return new IconLayerGenerator((IconMapLayer) layer);
		} else if (layer instanceof PiechartMapLayer) {
			return new PiechartLayerGenerator((PiechartMapLayer) layer);
		} else if( layer instanceof PolygonMapLayer) {
			return new PolygonLayerGenerator((PolygonMapLayer) layer);
		} else {
			throw new UnsupportedOperationException();
		}
	}


	private List<Indicator> queryIndicators(MapReportElement element) {
		
        // Get relevant indicators for the map markers
        Set<Integer> indicatorIds = new HashSet<Integer>(); 
        for (MapLayer maplayer : element.getLayers()) {
        	indicatorIds.addAll(maplayer.getIndicatorIds());
        }
        
		List<Indicator> indicators = Lists.newArrayList();
        for (Integer indicatorId : indicatorIds) {
        	indicators.add(indicatorDAO.findById(indicatorId));
        }
		return indicators;
	}

	private Set<IndicatorDTO> toDTOs(List<Indicator> indicators) {
		Set<IndicatorDTO> indicatorDTOs = new HashSet<IndicatorDTO>();        
        for(Indicator indicator : indicators) {
        	IndicatorDTO indicatorDTO = new IndicatorDTO();
        	indicatorDTO.setId(indicator.getId());
        	indicatorDTO.setName(indicator.getName());
        	
        	indicatorDTOs.add(indicatorDTO);
        }
		return indicatorDTOs;
	}

	private BaseMap findBaseMap(MapReportElement element, List<Indicator> indicators) {
		BaseMap baseMap = null;
        String baseMapId = element.getBaseMapId();
		if (element.getBaseMapId() == null || element.getBaseMapId().equals(MapReportElement.AUTO_BASEMAP)) {
        	baseMapId = defaultBaseMap(indicators);
        }
        if(PredefinedBaseMaps.isPredefinedMap(baseMapId)) {
        	baseMap = PredefinedBaseMaps.forId(baseMapId);
        } else {
        	baseMap = getBaseMap(baseMapId);
        }
		return baseMap;
	}

	private String defaultBaseMap(List<Indicator> indicators) {
		Set<Country> countries = queryCountries(indicators);
		if(countries.size() == 1) {
			Country country = countries.iterator().next();
			if("CD".equals(country.getCodeISO())) {
				return "admin";
			}
		}
		return GoogleBaseMap.ROADMAP.getId();			
	}

	private Set<Country> queryCountries(List<Indicator> indicators) {
		Set<Country> country = Sets.newHashSet();
		for(Indicator indicator : indicators) {
			country.add(indicator.getActivity().getDatabase().getCountry());
		}
		return country;
	}

	private BaseMap getBaseMap(String baseMapId) {
		BaseMapResult maps = dispatcher.execute(new GetBaseMaps());
		for(TileBaseMap map : maps.getBaseMaps()) {
			if(map.getId().equals(baseMapId)) {
				return map;
			}
		}
		logger.log(Level.SEVERE, "Could not find base map id=" +  baseMapId);
		
    	return TileBaseMap.createNullMap(baseMapId);
	}
}
