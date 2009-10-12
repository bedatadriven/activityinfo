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

import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.geom.Point;
import org.activityinfo.shared.report.model.MapIcon;

/**
 *
 * Factory for GoogleMap Icons originating from the
 * {@link org.activityinfo.server.servlet.MapIconServlet}
 *
 * @author Alex Bertram
 */
public class IconFactory {
    /**
     * Creates a Google Maps icon based on an ActivityInfo MapIcon
     *
     * @author Alex Bertram
     */
    public static Icon createIcon(MapIcon mapIcon) {

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

    public static Icon createBubble(int color, int radius) {
        StringBuilder sb = new StringBuilder();
        sb.append("icon?t=bubble&r=").append(radius).append("&c=").append(color);
        String iconUrl = sb.toString();
        int size = radius * 2;

        Icon icon = Icon.newInstance(Icon.DEFAULT_ICON);
		icon.setImageURL(iconUrl);
		icon.setIconSize(Size.newInstance(size, size));
		icon.setShadowSize(Size.newInstance(0, 0));
        Point anchor = Point.newInstance(radius, radius);
        icon.setIconAnchor(anchor);
		icon.setInfoWindowAnchor(anchor);
		icon.setPrintImageURL(iconUrl);
		icon.setMozPrintImageURL(iconUrl);

        return icon;
    }
}
