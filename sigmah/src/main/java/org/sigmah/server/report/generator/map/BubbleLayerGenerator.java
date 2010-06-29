/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

import org.sigmah.server.domain.SiteData;
import org.sigmah.server.report.generator.MapSymbol;
import org.sigmah.shared.report.content.*;
import org.sigmah.shared.report.model.*;

import java.util.*;

/**
 * @author Alex Bertram
 */
public class BubbleLayerGenerator implements LayerGenerator {

    private MapElement element;
    private BubbleMapLayer layer;

    public BubbleLayerGenerator(MapElement element, BubbleMapLayer layer) {
        this.element = element;
        this.layer = layer;

        // do sanity checks
        if(this.layer.isPie() && this.layer.getIndicatorIds().size()== 0) {
            throw new RuntimeException("Bubble layers styled as pies must have at least one indicator specified.");
        }
    }

    protected boolean hasValue(SiteData site, List<Integer> indicatorIds) {

        // if no indicators are specified, we count sites
        if(indicatorIds.size() == 0) {
            return true;
        }

        for(Integer indicatorId : indicatorIds) {
            Double indicatorValue = site.getIndicatorValue(indicatorId);
            if(indicatorValue != null) {
                return true;
            }
        }
        return false;
    }

    protected Double getValue(SiteData site, List<Integer> indicatorIds) {

        // if no indicators are specifid, we count sites.
        if(indicatorIds.size() == 0) {
            return 1.0;
        }

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
        RadiiCalculator radiiCalculator;
        if(layer.getScaling() == BubbleMapLayer.ScalingType.None ||
                layer.getMinRadius() == layer.getMaxRadius())
        {
            radiiCalculator = new FixedRadiiCalculator(layer.getMinRadius());
        } else if(layer.getScaling() == BubbleMapLayer.ScalingType.Graduated) {
            radiiCalculator = new GsLogCalculator(layer.getMinRadius(), layer.getMaxRadius());
        } else {
            radiiCalculator = new FixedRadiiCalculator(layer.getMinRadius());
        }

        // solve using the genetic algorithm
        List<Cluster> clusters;
        clusters = cluster(points, radiiCalculator);

        // add unmapped sites
        for(PointValue pv : unmapped) {
            content.getUnmappedSites().add(pv.site.getId());
        }

        // create the markers
        List<BubbleMapMarker> markers = new ArrayList<BubbleMapMarker>();
        for(Cluster cluster : clusters) {
            Point px = cluster.getPoint();
            LatLng latlng = cluster.latLngCentroid();

            BubbleMapMarker marker;
            if(layer.isPie()) {
                marker = new PieMapMarker();
                sumSlices((PieMapMarker) marker, cluster.getPointValues());
            } else {
                marker = new BubbleMapMarker();
            }
            for(PointValue pv : cluster.getPointValues()) {
                marker.getSiteIds().add(pv.site.getId());
            }
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

        // number markers if applicable
        if(layer.getNumbering() != BubbleMapLayer.NumberingType.None) {
            numberMarkers(markers);
        }

        content.getMarkers().addAll(markers);
    }

    public void generatePoints(
            List<SiteData> sites,
            TiledMap map,
            BubbleMapLayer layer,
            List<PointValue> mapped,
            List<PointValue> unmapped) {


        // if one of the dimensions is Indicator, then we will be potentially
        // generating several points per site. Otherwise, it is one point per site
        boolean byIndicator = layer.containsIndicatorDimension() &&
                 !layer.isPie();

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
                        PointValue pv = new PointValue(site,
                                createSymbol(site, layer.getColorDimensions()),
                                value, px);
                        if(layer.isPie()) {
                            calcSlices(pv, site);
                        }

                        (px==null ? unmapped : mapped).add(pv);
                    }
                }
            }
        }
    }

    private void calcSlices(PointValue pv, SiteData site) {
        Dimension dim = layer.getColorDimensions().get(0);
        pv.slices = new ArrayList<PieMapMarker.Slice>();
        if(dim.getType() == DimensionType.Indicator) {
            for(Integer integerId : layer.getIndicatorIds()) {
                EntityCategory indicatorCategory = new EntityCategory(integerId);
                Double value = site.getIndicatorValue(integerId);
                if(value != null && value != 0) {
                    PieMapMarker.Slice slice = new PieMapMarker.Slice();
                    slice.setValue(value);
                    slice.setCategory(indicatorCategory);

                    CategoryProperties props = dim.getCategories().get(indicatorCategory);
                    if(props != null && props.getColor() != null) {
                        slice.setColor(props.getColor());
                    } else {
                        slice.setColor(layer.getDefaultColor());
                    }
                    pv.slices.add(slice);
                }
            }
        }
    }

    private void sumSlices(PieMapMarker marker, List<PointValue> pvs) {
        Map<DimensionCategory, PieMapMarker.Slice> slices = new HashMap<DimensionCategory, PieMapMarker.Slice>();
        for(PointValue pv : pvs ) {
            for(PieMapMarker.Slice slice : pv.slices)  {
                PieMapMarker.Slice summedSlice = slices.get(slice.getCategory());
                if(summedSlice == null) {
                    summedSlice = new PieMapMarker.Slice(slice);
                    slices.put(slice.getCategory(), summedSlice);
                } else {
                    summedSlice.setValue(summedSlice.getValue() + slice.getValue());
                }
            }
        }
        marker.setSlices(new ArrayList<PieMapMarker.Slice>(slices.values()));
    }

    public MapSymbol createSymbol(SiteData site, List<Dimension> dimensions) {

        MapSymbol symbol = new MapSymbol();

        if(!layer.isPie()) {
            for(Dimension dimension : dimensions) {
                if(dimension.getType() == DimensionType.Partner) {
                    symbol.put(dimension, new EntityCategory(site.getPartnerId()));
                }
            }
        }
        return symbol;
    }

    public int findColor(MapSymbol symbol, BubbleMapLayer layer) {

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

    private List<Cluster> cluster(List<PointValue> points, RadiiCalculator radiiCalculator) {
        List<Cluster> clusters;
        if(layer.isClustered()) {

            MarkerGraph graph = new MarkerGraph(points, new IntersectionCalculator(layer.getMaxRadius()));

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
        return clusters;
    }

    private void numberMarkers(List<BubbleMapMarker> markers) {

        // sort the markers, left-to right, top to bottom so the label
        // sequence is spatially consistent
        Collections.sort(markers, new MapMarker.LRTBComparator());

        // create our label sequence based on the layer properties
        LabelSequence sequence;
        if(layer.getNumbering() == BubbleMapLayer.NumberingType.LatinAlphabet) {
            sequence = new LatinAlphaSequence();
        } else {
            sequence = new ArabicNumberSequence();
        }

        // add the labels
        for(BubbleMapMarker marker : markers) {
            marker.setLabel(sequence.next());
        }
    }

    public static class IntersectionCalculator implements MarkerGraph.IntersectionCalculator {
        private int radius;

        public IntersectionCalculator(int radius) {
            this.radius = radius;
        }

        public boolean intersects(MarkerGraph.Node a, MarkerGraph.Node b) {
            return a.getPoint().distance(b.getPoint()) < radius *2 &&
                    a.getPointValue().symbol.equals(b.getPointValue().symbol);
        }
    }
}
