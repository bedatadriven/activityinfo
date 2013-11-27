package org.activityinfo.client.util;

import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.util.mapping.Extents;
import org.discotools.gwt.leaflet.client.types.LatLng;
import org.discotools.gwt.leaflet.client.types.LatLngBounds;

public class LeafletUtil {

    public static LatLngBounds newLatLngBounds(Extents bounds) {
        LatLng southWest = new LatLng(bounds.getMinLat(), bounds.getMinLon());
        LatLng northEast = new LatLng(bounds.getMaxLat(), bounds.getMaxLon());
        return new LatLngBounds(southWest, northEast);
    }

    public static LatLng to(AiLatLng latLng) {
        return new LatLng(latLng.getLat(), latLng.getLng());
    }

    public static String color(String color) {
        if(color == null) {
            return "#FF0000";
        } else if(color.startsWith("#")) {
            return color;
        } else {
            return "#" + color;
        }
    }
}
