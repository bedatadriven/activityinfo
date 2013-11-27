package org.activityinfo.client.map;

import org.activityinfo.client.util.LeafletUtil;
import org.activityinfo.shared.report.content.BubbleMapMarker;
import org.activityinfo.shared.report.content.IconMapMarker;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.content.PieMapMarker;
import org.activityinfo.shared.report.content.PieMapMarker.SliceValue;
import org.activityinfo.shared.report.model.MapIcon;
import org.discotools.gwt.leaflet.client.Options;
import org.discotools.gwt.leaflet.client.marker.CircleMarker;
import org.discotools.gwt.leaflet.client.marker.Marker;
import org.discotools.gwt.leaflet.client.marker.MarkerOptions;
import org.discotools.gwt.leaflet.client.types.Icon;
import org.discotools.gwt.leaflet.client.types.IconOptions;
import org.discotools.gwt.leaflet.client.types.LatLng;
import org.discotools.gwt.leaflet.client.types.Point;

public class LeafletMarkerFactory {

    public static Marker create(MapMarker marker) {
        if (marker instanceof IconMapMarker) {
            return createIconMapMarker((IconMapMarker) marker);
        } else if (marker instanceof PieMapMarker) {
            return createPieMapMarker((PieMapMarker) marker);
        } else if (marker instanceof BubbleMapMarker) {
            return createBubbleMapMarker((BubbleMapMarker) marker);
        } else {
            return new Marker(toLatLng(marker), new Options());
        }
    }

    /**
     * Creates a Leaflet marker based on an ActivityInfo MapIcon
     */
    public static Marker createIconMapMarker(IconMapMarker model) {
        MapIcon iconModel = model.getIcon();
        String iconUrl = "mapicons/" + iconModel.getName() + ".png";
        
        IconOptions iconOptions = new IconOptions();
        iconOptions.setIconUrl(iconUrl);
        iconOptions.setIconAnchor(new Point(iconModel.getAnchorX(), iconModel.getAnchorY()));
        iconOptions.setIconSize(new Point(iconModel.getWidth(), iconModel.getHeight()));
              
        Options markerOptions = new MarkerOptions();
        markerOptions.setProperty("icon", new Icon(iconOptions));
        
        return new Marker(toLatLng(model), markerOptions);
    }

    private static LatLng toLatLng(MapMarker model) {
        return new LatLng(model.getLat(), model.getLng());
    }

    public static Marker createBubbleMapMarker(BubbleMapMarker marker) {
        
        Options options = new Options();
        options.setProperty("radius", marker.getRadius());
        options.setProperty("fill", true);
        options.setProperty("fillColor", LeafletUtil.color(marker.getColor()));
        options.setProperty("fillOpacity", marker.getAlpha());
        options.setProperty("color", LeafletUtil.color(marker.getColor())); // stroke color
        options.setProperty("opacity", 0.8); // stroke opacity
        
        return new CircleMarker(toLatLng(marker), options);
    }

    public static Marker createPieMapMarker(PieMapMarker marker) {
        StringBuilder sb = new StringBuilder();
        sb.append("icon?t=piechart&r=").append(marker.getRadius());
        for (SliceValue slice : marker.getSlices()) {
            sb.append("&value=").append(slice.getValue());
            sb.append("&color=").append(slice.getColor());
        }
        String iconUrl = sb.toString();
        int size = marker.getRadius() * 2;

        IconOptions iconOptions = new IconOptions();
        iconOptions.setIconUrl(iconUrl);
        iconOptions.setIconAnchor(new Point(marker.getRadius(), marker.getRadius()));
        iconOptions.setIconSize(new Point(size, size));
              
        Options markerOptions = new MarkerOptions();
        markerOptions.setProperty("icon", new Icon(iconOptions));
       
        return new Marker(toLatLng(marker), markerOptions);
    }

}
