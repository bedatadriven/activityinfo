/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.map;

import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import org.sigmah.shared.report.content.BubbleMapMarker;
import org.sigmah.shared.report.content.IconMapMarker;
import org.sigmah.shared.report.content.MapMarker;
import org.sigmah.shared.report.model.MapIcon;

/**
 * Factory for GoogleMap Icons originating from the
 * {@link org.sigmah.server.endpoint.gwtrpc.MapIconServlet}
 *
 * @author Alex Bertram
 */
public class IconFactory {

    public static Icon createIcon(MapMarker marker) {
        if (marker instanceof IconMapMarker) {
            return createIconMapMarker((IconMapMarker) marker);
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

        return icon;
    }


}
