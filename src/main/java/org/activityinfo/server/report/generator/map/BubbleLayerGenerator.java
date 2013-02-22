

package org.activityinfo.server.report.generator.map;

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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.activityinfo.server.report.generator.map.cluster.Cluster;
import org.activityinfo.server.report.generator.map.cluster.Clusterer;
import org.activityinfo.server.report.generator.map.cluster.ClustererFactory;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.BubbleLayerLegend;
import org.activityinfo.shared.report.content.BubbleMapMarker;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.content.Point;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.MapSymbol;
import org.activityinfo.shared.report.model.PointValue;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;
import org.activityinfo.shared.report.model.layers.ScalingType;
import org.activityinfo.shared.util.mapping.Extents;

public class BubbleLayerGenerator extends PointLayerGenerator<BubbleMapLayer> {


    public BubbleLayerGenerator(BubbleMapLayer layer) {
    	super(layer);
    }

    public Extents calculateExtents() {
        // PRE---PASS - calculate extents of sites WITH non-zero
        // values for this indicator

        Extents extents = Extents.emptyExtents();
        for(SiteDTO site : sites) {
            if(site.hasLatLong() && hasValue(site, layer.getIndicatorIds())) {
                extents.grow(site.getLatitude(), site.getLongitude());
            }
        }

        return extents;
    }

    public Margins calculateMargins() {
        return new Margins(layer.getMaxRadius());
    }

    public void generate(TiledMap map, MapContent content) {
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

        BubbleIntersectionCalculator intersectionCalculator = new BubbleIntersectionCalculator(layer.getMaxRadius());
        Clusterer clusterer = ClustererFactory.fromClustering
        	(layer.getClustering(), radiiCalculator, intersectionCalculator);

        // create the list of input point values
        List<PointValue> points = new ArrayList<PointValue>();
        List<PointValue> unmapped = new ArrayList<PointValue>();
        generatePoints(sites, map, layer, clusterer, points, unmapped);

        
        // Cluster points by the clustering algorithm set in the layer 
        List<Cluster> clusters = clusterer.cluster(map, points);
        
        // add unmapped sites
        for(PointValue pv : unmapped) {
            content.getUnmappedSites().add(pv.getSite().getId());
        }

        BubbleLayerLegend legend = new BubbleLayerLegend();
        legend.setDefinition(layer);
        
        // create the markers
        List<BubbleMapMarker> markers = new ArrayList<BubbleMapMarker>();
        for(Cluster cluster : clusters) {
            Point px = cluster.getPoint();
            AiLatLng latlng = map.fromPixelToLatLng(px);
            BubbleMapMarker marker =  new BubbleMapMarker();

            for(PointValue pv : cluster.getPointValues()) {
                marker.getSiteIds().add(pv.getSite().getId());
            }
            marker.setX(px.getX());
            marker.setY(px.getY());
            marker.setValue(cluster.sumValues());
            marker.setRadius((int)cluster.getRadius());
            marker.setLat(latlng.getLat());
            marker.setLng(latlng.getLng());
            marker.setAlpha(layer.getAlpha());
            marker.setTitle(formatTitle(cluster));
            marker.setIndicatorIds(new HashSet<Integer>(layer.getIndicatorIds()));
            marker.setClusterAmount(cluster.getPointValues().size());
            marker.setClustering(layer.getClustering());
            
            marker.setColor(layer.getBubbleColor());
            
            if(marker.getValue() < legend.getMinValue()) {
            	legend.setMinValue(marker.getValue());
            }
            if(marker.getValue() > legend.getMaxValue()) {
            	legend.setMaxValue(marker.getValue());
            }

            markers.add(marker);
        }
        
        // sort order by symbol radius descending
        // (this assures that smaller symbols are drawn on
        // top of larger ones)
        Collections.sort(markers, new Comparator<MapMarker>() {
            public int compare(MapMarker o1, MapMarker o2) {
                if (o1.getSize() > o2.getSize()) {
                    return -1;
                } else if (o1.getSize() < o2.getSize()) {
                    return 1;
                }
                return 0;
            }
        });

        
        // number markers if applicable
        if(layer.getLabelSequence() != null) {
            numberMarkers(markers);
        }

        content.addLegend(legend);
        content.getMarkers().addAll(markers);
    }

    private String formatTitle(Cluster cluster) {
		if(cluster.hasTitle()) {
			return cluster.getTitle() + " - " + cluster.sumValues();
		} else {
			return Double.toString(cluster.sumValues());
		}
	}

	public void generatePoints(
            List<SiteDTO> sites,
            TiledMap map,
            BubbleMapLayer layer,
            Clusterer clusterer,
            List<PointValue> mapped,
            List<PointValue> unmapped) {

    	
        for(SiteDTO site : sites) {

            if(hasValue(site, layer.getIndicatorIds())) {

                Point px = null;
                if(site.hasLatLong())  {
                    px = map.fromLatLngToPixel(new AiLatLng(site.getLatitude(), site.getLongitude()));
                }
      
                Double value = getValue(site, layer.getIndicatorIds());
                if(value != null && value != 0) {
                    PointValue pv = new PointValue(site,
                            createSymbol(site, layer.getColorDimensions()),
                            value, px);
                    
                    // TODO: add AdminLevel to pointvalue
                    if(clusterer.isMapped(site)) {
                    	mapped.add(pv);
                    } else {
                    	unmapped.add(pv);
                    }
                }
            }
        }
    }

	// this was too complicated. 
	// we should be able to achieve the same result using filters on the labels
	// --> schedule to remove
	@Deprecated
    public MapSymbol createSymbol(SiteDTO site, List<Dimension> dimensions) {
        MapSymbol symbol = new MapSymbol();

        for(Dimension dimension : dimensions) {
            if(dimension.getType() == DimensionType.Partner) {
                symbol.put(dimension, new EntityCategory(site.getPartnerId()));
            }
        }
        return symbol;
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
}
