/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator.map;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.server.report.generator.map.cluster.Cluster;
import org.activityinfo.server.report.generator.map.cluster.Clusterer;
import org.activityinfo.server.report.generator.map.cluster.ClustererFactory;
import org.activityinfo.server.report.generator.map.cluster.genetic.MarkerGraph.IntersectionCalculator;
import org.activityinfo.server.report.generator.map.cluster.genetic.MarkerGraph.Node;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.IconLayerLegend;
import org.activityinfo.shared.report.content.IconMapMarker;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.Point;
import org.activityinfo.shared.report.model.MapIcon;
import org.activityinfo.shared.report.model.PointValue;
import org.activityinfo.shared.report.model.layers.IconMapLayer;
import org.activityinfo.shared.util.mapping.Extents;


public class IconLayerGenerator 
	extends 
		AbstractLayerGenerator
	implements 
		LayerGenerator {

    private final IconMapLayer layer;

    private MapIcon icon;
	private List<SiteDTO> sites;


    public IconLayerGenerator(IconMapLayer layer, List<SiteDTO> sites) {
        this.layer = layer;
        this.sites=sites;

        this.icon = new MapIcon(layer.getIcon(), 32, 37, 16, 35);
    }

    public boolean meetsCriteria(SiteDTO site) {
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

    @Override
	public Extents calculateExtents() {
        Extents extents = Extents.emptyExtents();
        for(SiteDTO site : sites) {
            if(meetsCriteria(site) && site.hasLatLong()) {
                extents.grow(site.getLatitude(), site.getLongitude());
            }
        }
        return extents;
    }

    @Override
	public Margins calculateMargins() {
        return new Margins(
                icon.getAnchorX(),
                icon.getAnchorY(),
                icon.getHeight() -icon.getAnchorY(),
                icon.getWidth() - icon.getAnchorX());
    }

    @Override
	public void generate(TiledMap map, MapContent content) {
        List<PointValue> points = new ArrayList<PointValue>();
        IconRectCalculator rectCalculator = new IconRectCalculator(icon);

        for(SiteDTO site : sites) {
            if(meetsCriteria(site)) {
                if(site.hasLatLong()) {
                    Point point = map.fromLatLngToPixel(new AiLatLng(site.getLatitude(), site.getLongitude()));
                    points.add(new PointValue(site, point, rectCalculator.iconRect(point), getValue(site, layer.getIndicatorIds())));
                } else {
                    content.getUnmappedSites().add(site.getId());
                }
            }
        }
        
        IntersectionCalculator intersectionCalculator = new IntersectionCalculator() {
			@Override
			public boolean intersects(Node a, Node b) {
	        	  return a.getPointValue().getIconRect().intersects(b.getPointValue().getIconRect());
			}
		};
        
		Clusterer clusterer = ClustererFactory.fromClustering(layer.getClustering(), rectCalculator, intersectionCalculator);
		
        List<Cluster> clusters = clusterer.cluster(map, points);
        createMarkersFrom(clusters, map, content);
        
        IconLayerLegend legend = new IconLayerLegend();
        legend.setDefinition(layer);
        
		content.addLegend(legend);
    }

	private void createMarkersFrom(List<Cluster> clusters, TiledMap map, MapContent content) {
		for(Cluster cluster : clusters) {
            IconMapMarker marker = new IconMapMarker();
            marker.setX(cluster.getPoint().getX());
            marker.setY(cluster.getPoint().getY());
            AiLatLng latlng = map.fromPixelToLatLng(cluster.getPoint());
            marker.setLat(latlng.getLat());
            marker.setLng(latlng.getLng());
            marker.setTitle(formatTitle(cluster));
            marker.setIcon(icon);
            marker.setIndicatorId(layer.getIndicatorIds().get(0));
            content.getMarkers().add(marker);
        }
	}

	protected String formatTitle(Cluster cluster) {
		if (cluster.getPointValues() != null) {
			return Double.toString(cluster.sumValues());
		}
		return "";
	}
	
	
}
