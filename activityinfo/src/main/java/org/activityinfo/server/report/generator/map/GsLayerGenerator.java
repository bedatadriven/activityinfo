package org.activityinfo.server.report.generator.map;

import org.activityinfo.shared.report.model.*;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.content.*;
import org.activityinfo.shared.report.content.Point;
import org.activityinfo.server.report.generator.MapSymbol;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/*
 * @author Alex Bertram
 */

public class GsLayerGenerator implements LayerGenerator {

    private MapElement element;
    private GsMapLayer layer;

    public GsLayerGenerator(MapElement element, GsMapLayer layer) {
        this.element = element;
        this.layer = layer;
    }


    protected boolean hasValue(SiteData site, List<Integer> indicatorIds) {
        for(Integer indicatorId : indicatorIds) {
            Double indicatorValue = site.getIndicatorValue(indicatorId);
            if(indicatorValue != null) {
                return true;
            }
        }
        return false;
    }

    protected Double getValue(SiteData site, List<Integer> indicatorIds) {
        Double value = null;
        for(Integer indicatorId : indicatorIds) {
            Double indicatorValue = site.getIndicatorValue(indicatorId);
            if(indicatorValue != null) {
                if(value == null) {
                    value = indicatorValue;
                } else {
                    value += indicatorValue;
                }
            }
        }
        return value;
    }

    public Extents calculateExtents(List<SiteData> sites) {

        // PRE---PASS - calculate extents of sites WITH non-zero
        // values for this indicator

        Extents extents = Extents.emptyExtents();
        for(SiteData site : sites) {

            if(site.hasLatLong() && hasValue(site, layer.getIndicatorIds())) {

                extents.grow(site.getLatitude(), site.getLongitude());
            }
        }


        return extents;
    }

    public Margins calculateMargins() {
        return new Margins(layer.getMaxRadius());
    }

    public void generate(List<SiteData> sites, TiledMap map, MapContent content) {


        // create the list of input point values
        List<PointValue> points = new ArrayList<PointValue>();
        List<PointValue> unmapped = new ArrayList<PointValue>();

        generatePoints(sites, map, layer, points, unmapped);

        // define our symbol scaling
        GsLogCalculator radiiCalculator = new GsLogCalculator(layer.getMinRadius(),
                layer.getMaxRadius());

        // solve using the genetic algorithm

        List<Cluster> clusters;
        if(layer.isClustered()) {


            MarkerGraph graph = new MarkerGraph(points,
                    new IntersectionCalculator(layer.getMaxRadius()));

            GeneticSolver solver = new GeneticSolver();
            clusters = solver.solve(
                    graph,
                    radiiCalculator,
                    new CircleFitnessFunctor(),
                    UpperBoundsCalculator.calculate(graph,
                         new FixedRadiiCalculator(layer.getMinRadius())));
        } else {
            clusters = new ArrayList<Cluster>();
            for(PointValue point : points) {
                clusters.add(new Cluster(point));
            }
            radiiCalculator.calculate(clusters);
        }


        // add unmapped sites
        for(PointValue pv : unmapped) {
            content.getUnmappedSites().add(pv.site.getId());
        }

        // create the markers
        List<MapMarker> markers = new ArrayList<MapMarker>();
        for(Cluster cluster : clusters) {
            Point px = cluster.getPoint();
            LatLng latlng = cluster.latLngCentroid();

            MapMarker marker = new MapMarker();
            marker.setX(px.getX());
            marker.setY(px.getY());
            marker.setValue(cluster.sumValues());
            marker.setRadius((int)cluster.getRadius());
            marker.setLat(latlng.getLat());
            marker.setLng(latlng.getLng());
            marker.setAlpha(layer.getAlpha());

            marker.setColor(findColor(cluster.getPointValues().get(0).symbol, layer));

            markers.add(marker);
        }


        // FINALLY: assign default colors to symbols

        content.getMarkers().addAll(markers);

    }


    public void generatePoints(
            List<SiteData> sites,
            TiledMap map,
            GsMapLayer layer,
            List<PointValue> mapped,
            List<PointValue> unmapped) {


        // if one of the dimensions is Indicator, then we will be potentially
        // generating several points per site. Otherwise, it is one point per site
        boolean byIndicator = layer.containsIndicatorDimension();

        for(SiteData site : sites) {

            if(hasValue(site, layer.getIndicatorIds())) {

                Point px = null;
                if(site.hasLatLong())  {
                    px = map.fromLatLngToPixel(new LatLng(site.getLatitude(), site.getLongitude()));
                }
                if(byIndicator) {

                    for(Integer indicatorId : layer.getIndicatorIds()) {
                        if(site.getIndicatorValue(indicatorId) != null) {
                            MapSymbol symbol = createSymbol(site, layer.getColorDimensions());
                            symbol.put(new Dimension(DimensionType.Indicator),
                                    new EntityCategory(indicatorId,null));

                            Double value = site.getIndicatorValue(indicatorId);
                            if(value != null && value != 0) {
                                (px==null ? unmapped : mapped).add(new PointValue(site, symbol, value, px));
                            }
                        }
                    }
                } else {

                    Double value = getValue(site, layer.getIndicatorIds());
                    if(value != null && value != 0) {
                        (px==null ? unmapped : mapped).add(new PointValue(site,
                                createSymbol(site, layer.getColorDimensions()),
                                value, px));
                    }

                }
            }
        }
    }

    public MapSymbol createSymbol(SiteData site, List<Dimension> dimensions) {

        MapSymbol symbol = new MapSymbol();

        for(Dimension dimension : dimensions) {
            if(dimension.getType() == DimensionType.Partner) {
                symbol.put(dimension, new EntityCategory(site.getPartnerId()));
            }
        }

        return symbol;
    }

    public int findColor(MapSymbol symbol, GsMapLayer layer) {

        if(layer.getColorDimensions().size() == 0) {
            return layer.getDefaultColor();
        } else {
            Dimension dimension = layer.getColorDimensions().get(0);
            DimensionCategory category = symbol.get(dimension);
            CategoryProperties categoryProperties = dimension.getCategories().get(category);
            if(categoryProperties == null) {
                return layer.getDefaultColor();
            } else {
                return categoryProperties.getColor();
            }
        }
    }

    public static class IntersectionCalculator implements MarkerGraph.IntersectionCalculator {
        private int maxRadius;

        public IntersectionCalculator(int maxRadius) {
            this.maxRadius = maxRadius;
        }

        public boolean intersects(MarkerGraph.Node a, MarkerGraph.Node b) {
            return a.getPoint().distance(b.getPoint()) < maxRadius *2 &&
                    a.getPointValue().symbol.equals(b.getPointValue().symbol);
        }
    }
}
