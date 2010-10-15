/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator;

import com.google.inject.Inject;
import org.sigmah.server.dao.BaseMapDAO;
import org.sigmah.server.dao.PivotDAO;
import org.sigmah.server.domain.SiteData;
import org.sigmah.server.report.generator.map.*;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dao.SiteTableDAO;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.report.content.Extents;
import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.content.MapMarker;
import org.sigmah.shared.report.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class MapGenerator extends ListGenerator<MapElement> {

    private final BaseMapDAO baseMapDAO;

    @Inject
    public MapGenerator(PivotDAO pivotDAO, SiteTableDAO siteDAO, BaseMapDAO baseMapDAO) {
        super(pivotDAO, siteDAO);
        this.baseMapDAO = baseMapDAO;
    }

    public void generate(User user, MapElement element, Filter inheritedFilter, DateRange dateRange) {

        Filter filter = resolveElementFilter(element, dateRange);
        Filter effectiveFilter = inheritedFilter == null ? filter : new Filter(inheritedFilter, filter);

        List<SiteData> sites = siteDAO.query(
                user,
                effectiveFilter,
                null,
                new SiteDataBinder(),
                SiteTableDAO.RETRIEVE_ALL, 0, -1);

        MapContent content = new MapContent();
        content.setFilterDescriptions(generateFilterDescriptions(filter, Collections.<DimensionType>emptySet(), user));

        // Set up layer generators
        List<LayerGenerator> layerGenerators = new ArrayList<LayerGenerator>();
        for (MapLayer layer : element.getLayers()) {
            if (layer instanceof BubbleMapLayer) {
                layerGenerators.add(new BubbleLayerGenerator(element,
                        (BubbleMapLayer) layer));
            } else if (layer instanceof IconMapLayer) {
                layerGenerators.add(new IconLayerGenerator(element,
                        (IconMapLayer) layer));
            }
        }

        // FIRST PASS: calculate extents and margins
        Extents extents = Extents.emptyExtents();
        Margins margins = new Margins(0);
        for (LayerGenerator layerGtor : layerGenerators) {
            extents.grow(layerGtor.calculateExtents(sites));
            margins.grow(layerGtor.calculateMargins());
        }

        // Now we're ready to calculate the zoom level
        // and the projection
        int width = element.getWidth();
        int height = element.getHeight();
        int zoom = TileMath.zoomLevelForExtents(extents,
                width,
                height);

        // Retrieve the basemap and clamp zoom level
        BaseMap baseMap = baseMapDAO.getBaseMap(element.getBaseMapId());
        if (baseMap == null) {
            throw new RuntimeException("Could not find base map id=" + element.getBaseMapId());
        }
        if (zoom < baseMap.getMinZoom()) {
            zoom = baseMap.getMinZoom();
        }
        if (zoom > baseMap.getMaxZoom()) {
            zoom = baseMap.getMaxZoom();
        }

        TiledMap map = new TiledMap(width, height, extents.center(), zoom);
        content.setBaseMap(baseMap);
        content.setZoomLevel(zoom);
        content.setExtents(extents);

        // Generate the actual content
        for (LayerGenerator layerGtor : layerGenerators) {
            layerGtor.generate(sites, map, content);
        }

        // sort order by symbol radius descending
        // (this assures that smaller symbols are drawn on
        // top of larger ones)
        Collections.sort(content.getMarkers(), new Comparator<MapMarker>() {
            public int compare(MapMarker o1, MapMarker o2) {
                if (o1.getSize() > o2.getSize()) {
                    return -1;
                } else if (o1.getSize() < o2.getSize()) {
                    return 1;
                }
                return 0;
            }
        });

        element.setContent(content);

    }
}
