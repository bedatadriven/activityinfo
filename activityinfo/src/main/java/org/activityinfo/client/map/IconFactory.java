/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.client.map;

import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import org.activityinfo.shared.report.content.BubbleMapMarker;
import org.activityinfo.shared.report.content.IconMapMarker;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.model.MapIcon;

/**
 *
 * Factory for GoogleMap Icons originating from the
 * {@link org.activityinfo.server.servlet.MapIconServlet}
 *
 * @author Alex Bertram
 */
public class IconFactory {

    public static Icon createIcon(MapMarker marker) {
        if(marker instanceof IconMapMarker) {
            return createIconMapMarker((IconMapMarker) marker);
        } else if(marker instanceof BubbleMapMarker) {
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

        return icon;
    }


}
