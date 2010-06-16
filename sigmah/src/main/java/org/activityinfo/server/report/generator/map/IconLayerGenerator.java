package org.activityinfo.server.report.generator.map;

import org.activityinfo.server.domain.SiteData;
import org.activityinfo.shared.report.content.*;
import org.activityinfo.shared.report.model.IconMapLayer;
import org.activityinfo.shared.report.model.MapElement;
import org.activityinfo.shared.report.model.MapIcon;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Alex Bertram
 */
public class IconLayerGenerator implements LayerGenerator {

    private final MapElement element;
    private final IconMapLayer layer;

    private MapIcon icon;


    public IconLayerGenerator(MapElement element, IconMapLayer layer) {
        this.element = element;
        this.layer = layer;

        this.icon = new MapIcon(layer.getIcon(), 32, 37, 16, 35);
    }

    public boolean meetsCriteria(SiteData site) {
        if(layer.getIndicatorIds().size() != 0) {
            for(Integer indicatorId : layer.getIndicatorIds()) {
                Double indicatorValue = site.getIndicatorValue(indicatorId);
                if(indicatorValue != null && indicatorValue > 0) {
                    return true;
                }
            }
            return false;
        } else {
            return layer.getActivityIds().contains(site.getActivityId());
        }
    }

    public Extents calculateExtents(List<SiteData> sites) {

        Extents extents = Extents.emptyExtents();
        for(SiteData site : sites) {
            if(meetsCriteria(site) && site.hasLatLong()) {
                extents.grow(site.getLatitude(), site.getLongitude());
            }
        }
        return extents;
    }

    public Margins calculateMargins() {
        return new Margins(
                icon.getAnchorX(),
                icon.getAnchorY(),
                icon.getHeight() -icon.getAnchorY(),
                icon.getWidth() - icon.getAnchorX());
    }



    public void generate(List<SiteData> sites, TiledMap map, MapContent content) {

        List<PointValue> points = new ArrayList<PointValue>();

        IconRectCalculator rectCalculator = new IconRectCalculator(icon);

        for(SiteData site : sites) {
            if(meetsCriteria(site)) {

                if(site.hasLatLong()) {

                    Point point = map.fromLatLngToPixel(new LatLng(site.getLatitude(), site.getLongitude()));
                    points.add(new PointValue(site, point, rectCalculator.iconRect(point)));

                } else {

                    content.getUnmappedSites().add(site.getId());
                }

            }
        }

        if(layer.isClustered()) {

            MarkerGraph graph = new MarkerGraph(points, new MarkerGraph.IntersectionCalculator() {
                public boolean intersects(MarkerGraph.Node a, MarkerGraph.Node b) {
                    return a.getPointValue().iconRect.intersects(b.getPointValue().iconRect);
                }
            });

            GeneticSolver solver = new GeneticSolver();

            List<Cluster> clusters = solver.solve(graph, rectCalculator, new RectFitnessFunctor(),
                    UpperBoundsCalculator.calculate(graph, rectCalculator));

            for(Cluster cluster : clusters) {
                IconMapMarker marker = new IconMapMarker();
                marker.setX(cluster.getPoint().getX());
                marker.setY(cluster.getPoint().getY());
                LatLng latlng = cluster.latLngCentroid();
                marker.setLat(latlng.getLat());
                marker.setLng(latlng.getLng());
                marker.setIcon(icon);
                content.getMarkers().add(marker);
            }
        } else {

            for(PointValue point : points) {
                IconMapMarker marker = new IconMapMarker();
                marker.setX(point.px.getX());
                marker.setY(point.px.getY());
                marker.setLat(point.site.getLatitude());
                marker.setLng(point.site.getLongitude());
                marker.setIcon(icon);
                content.getMarkers().add(marker);
            }
        }
    }
}
