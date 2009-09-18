package org.activityinfo.server.report.generator;

import org.activityinfo.server.dao.BaseMapDAO;
import org.activityinfo.server.dao.hibernate.PivotDAO;
import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.map.*;
import org.activityinfo.shared.map.BaseMap;
import org.activityinfo.shared.report.content.*;
import org.activityinfo.shared.report.model.*;

import java.util.*;
import java.util.List;

import com.google.inject.Inject;
/*
 * @author Alex Bertram
 */

public class MapGenerator extends ListGenerator<MapElement> {

    private final BaseMapDAO baseMapDAO;

    @Inject
    public MapGenerator(PivotDAO pivotDAO, SiteTableDAO siteDAO, BaseMapDAO baseMapDAO) {
        super(pivotDAO, siteDAO);
        this.baseMapDAO = baseMapDAO;
    }

    public void generate(User user, MapElement element, Filter inheritedFilter, Map<String, Object> parameterValues) {

        Filter filter = ParamFilterHelper.resolve(element.getFilter(), parameterValues);
        Filter effectiveFilter = inheritedFilter == null ? filter : new Filter(inheritedFilter, filter);

        List<SiteData> sites = siteDAO.query(
                user,
                FilterCriterionBridge.resolveCriterion(effectiveFilter),
                null,
                new SiteDataBinder(),
                SiteTableDAO.RETRIEVE_ALL, 0, -1);

        MapContent content = new MapContent();
        content.setFilterDescriptions(generateFilterDescriptions(filter, Collections.<DimensionType>emptySet()));

        // Set up layer generators
        List<LayerGenerator> layerGenerators = new ArrayList<LayerGenerator>();
        for(MapLayer layer : element.getLayers()) {
            if(layer instanceof GsMapLayer) {
                layerGenerators.add(new GsLayerGenerator(element,
                        (GsMapLayer) layer));
            } else if(layer instanceof IconMapLayer) {
                layerGenerators.add(new IconLayerGenerator(element,
                        (IconMapLayer)layer));
            }
        }


        // FIRST PASS: calculate extents and margins

        Extents extents = Extents.emptyExtents();
        Margins margins = new Margins(0);
        for(LayerGenerator layerGtor : layerGenerators) {
            extents.grow(layerGtor.calculateExtents(sites));
            margins.grow(layerGtor.calculateMargins());
        }

        // Now we're ready to calculate the zoom level
        // and the projection

        int width = element.getWidth();
        int height = element.getHeight() ;
        int zoom = TileMath.zoomLevelForExtents(extents,
                width,
                height);

        // Retrieve the basemap and clamp zoom level
        BaseMap baseMap = baseMapDAO.getBaseMap(element.getBaseMapId());
        if(zoom < baseMap.getMinZoom())
            zoom = baseMap.getMinZoom();
        if(zoom > baseMap.getMaxZoom())
            zoom = baseMap.getMaxZoom();

        TiledMap map = new TiledMap(width, height, extents.center(), zoom);
        content.setBaseMap(baseMap);
        content.setZoomLevel(zoom);
        content.setExtents(extents);

        // Generate the actual content

        for(LayerGenerator layerGtor : layerGenerators) {
            layerGtor.generate(sites, map, content);
        }

        // sort order by symbol radius descending
        // (this assures that smaller symbols are drawn on
        // top of larger ones)
        Collections.sort(content.getMarkers(), new Comparator<MapMarker>() {
            public int compare(MapMarker o1, MapMarker o2) {
                if(o1.getRadius() > o2.getRadius())
                    return -1;
                else if(o1.getRadius() < o2.getRadius())
                    return 1;
                return 0;
            }
        });

        element.setContent(content);

    }
}
