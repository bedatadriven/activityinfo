/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

import org.sigmah.server.report.ClusterImpl;
import org.sigmah.server.report.generator.map.cluster.ClustererFactory;
import org.sigmah.shared.report.content.*;
import org.sigmah.shared.report.model.*;
import org.sigmah.shared.report.model.clustering.Cluster;
import org.sigmah.shared.report.model.clustering.Clusterer;
import org.sigmah.shared.report.model.labeling.ArabicNumberSequence;
import org.sigmah.shared.report.model.labeling.LabelSequence;
import org.sigmah.shared.report.model.labeling.LatinAlphaSequence;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.MapLayer;
import org.sigmah.shared.report.model.layers.ScalingType;
import org.sigmah.shared.util.mapping.Extents;

import java.util.*;

public class BubbleLayerGenerator extends AbstractLayerGenerator {

    private MapReportElement element;
    private BubbleMapLayer layer;

    public BubbleLayerGenerator(MapReportElement element, BubbleMapLayer layer) {
        this.element = element;
        this.layer = layer;
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
        if(layer.getScaling() == ScalingType.None ||
                layer.getMinRadius() == layer.getMaxRadius())
        {
            radiiCalculator = new FixedRadiiCalculator(layer.getMinRadius());
        } else if(layer.getScaling() == ScalingType.Graduated) {
            radiiCalculator = new GsLogCalculator(layer.getMinRadius(), layer.getMaxRadius());
        } else {
            radiiCalculator = new FixedRadiiCalculator(layer.getMinRadius());
        }

        IntersectionCalculator intersectionCalculator = new IntersectionCalculator(layer.getMaxRadius());
        Clusterer clusterer = ClustererFactory.fromClustering
        	(layer.getClustering(), radiiCalculator, points, intersectionCalculator);
        
        // Cluster points by the clustering algorithm set in the layer 
        List<Cluster> clusters = clusterer.cluster();
        
        // add unmapped sites
        for(PointValue pv : unmapped) {
            content.getUnmappedSites().add(pv.site.getId());
        }

        // create the markers
        List<BubbleMapMarker> markers = new ArrayList<BubbleMapMarker>();
        for(Cluster cluster : clusters) {
            Point px = cluster.getPoint();
            LatLng latlng = cluster.latLngCentroid();
            BubbleMapMarker marker =  new BubbleMapMarker();

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

            //marker.setColor(findColor(cluster.getPointValues().get(0).symbol, layer));
            marker.setColor(layer.getLabelColor());

            markers.add(marker);
        }

        // number markers if applicable
        if(layer.getLabelSequence() != null) {
            numberMarkers(markers);
        }

        content.getMarkers().addAll(markers);
    }

    /*
     * 
     */
    public void generatePoints(
            List<SiteData> sites,
            TiledMap map,
            BubbleMapLayer layer,
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
                        PointValue pv = new PointValue(site,
                                createSymbol(site, layer.getColorDimensions()),
                                value, px);
                        
                        // TODO: add AdminLevel to pointvalue
                        
                        (px==null ? unmapped : mapped).add(pv);
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

    private void numberMarkers(List<BubbleMapMarker> markers) {

        // sort the markers, left-to right, top to bottom so the label
        // sequence is spatially consistent
        Collections.sort(markers, new MapMarker.LRTBComparator());

        // add the labels
        for(BubbleMapMarker marker : markers) {
            marker.setLabel(layer.getLabelSequence().next());
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
