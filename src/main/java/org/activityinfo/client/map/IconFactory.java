

package org.activityinfo.client.map;

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

import org.activityinfo.shared.report.content.BubbleMapMarker;
import org.activityinfo.shared.report.content.IconMapMarker;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.content.PieMapMarker;
import org.activityinfo.shared.report.content.PieMapMarker.SliceValue;
import org.activityinfo.shared.report.model.MapIcon;

import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;

/**
 * Factory for GoogleMap Icons originating from the
 * {@link org.activityinfo.server.endpoint.gwtrpc.MapIconServlet}
 *
 * @author Alex Bertram
 */
public final class IconFactory {

    private IconFactory() {}

    public static Icon createIcon(MapMarker marker) {
        if (marker instanceof IconMapMarker) {
            return createIconMapMarker((IconMapMarker) marker);
        } else if (marker instanceof PieMapMarker) {
            return createPieMapMarker((PieMapMarker) marker);
        } else if (marker instanceof BubbleMapMarker) {
            return createBubbleMapMarker((BubbleMapMarker) marker);
        } else {
            return Icon.DEFAULT_ICON;
        }
    }

    /**
     * Creates a Google Maps icon based on an ActivityInfo MapIcon
     *
     * @author Alex Bertram
     */
    public static Icon createIconMapMarker(IconMapMarker marker) {

        MapIcon mapIcon = marker.getIcon();
        String iconUrl = "mapicons/" + mapIcon.getName() + ".png";

        Icon icon = Icon.newInstance(Icon.DEFAULT_ICON);
        icon.setImageURL(iconUrl);
        icon.setIconSize(Size.newInstance(mapIcon.getWidth(), mapIcon.getHeight()));
        icon.setShadowSize(Size.newInstance(0, 0));
        Point anchor = Point.newInstance(mapIcon.getAnchorX(), mapIcon.getAnchorY());
        icon.setIconAnchor(anchor);
        icon.setInfoWindowAnchor(anchor);
        icon.setPrintImageURL(iconUrl + "&chof=gif");
        icon.setMozPrintImageURL(iconUrl);
        return icon;
    }

    public static Icon createBubbleMapMarker(BubbleMapMarker marker) {
        StringBuilder sb = new StringBuilder();
        sb.append("icon?t=bubble&r=").append(marker.getRadius()).append("&c=").append(marker.getColor());
        String iconUrl = sb.toString();
        int size = marker.getRadius() * 2;

        Icon icon = Icon.newInstance(Icon.DEFAULT_ICON);
        icon.setImageURL(iconUrl);
        icon.setIconSize(Size.newInstance(size, size));
        icon.setShadowSize(Size.newInstance(0, 0));
        Point anchor = Point.newInstance(marker.getRadius(), marker.getRadius());
        icon.setIconAnchor(anchor);
        icon.setInfoWindowAnchor(anchor);
        icon.setPrintImageURL(iconUrl);
        icon.setMozPrintImageURL(iconUrl);
        icon.setImageMap(GoogleChartsIconBuilder.createCircleImageMap(size, size, 10));
        return icon;
    }
    
    public static Icon createPieMapMarker(PieMapMarker marker) {
        StringBuilder sb = new StringBuilder();
        sb.append("icon?t=piechart&r=").append(marker.getRadius());
        for (SliceValue slice : marker.getSlices()) {
        	sb.append("&value=").append(slice.getValue());
        	sb.append("&color=").append(slice.getColor());
        }
        String iconUrl = sb.toString();
        int size = marker.getRadius() * 2;

        Icon icon = Icon.newInstance(Icon.DEFAULT_ICON);
        icon.setImageURL(iconUrl);
        icon.setIconSize(Size.newInstance(size, size));
        icon.setShadowSize(Size.newInstance(0, 0));
        Point anchor = Point.newInstance(marker.getRadius(), marker.getRadius());
        icon.setIconAnchor(anchor);
        icon.setInfoWindowAnchor(anchor);
        icon.setPrintImageURL(iconUrl);
        icon.setMozPrintImageURL(iconUrl);
        icon.setImageMap(GoogleChartsIconBuilder.createCircleImageMap(size, size, 10));
        
        return icon;
    }
}
