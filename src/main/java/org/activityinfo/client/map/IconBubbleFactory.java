package org.activityinfo.client.map;

import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.geom.Point;
/*
 * @author Alex Bertram
 */

public class IconBubbleFactory {


    public Icon createBubble(int radius) {

        String baseUrl = "../mapicon?t=bubble&";
        String iconUrl = baseUrl + "&radius=" + radius + "&ext=.png";

        Icon icon = Icon.newInstance(iconUrl);
        icon.setIconSize(Size.newInstance(radius*2, radius*2));
        icon.setShadowSize(Size.newInstance(0,0));
        icon.setIconAnchor(Point.newInstance(radius, radius));
        icon.setInfoWindowAnchor(Point.newInstance(radius, radius));
        icon.setPrintImageURL(iconUrl);
        icon.setMozPrintImageURL(iconUrl);
       // icon.setTransparentImageURL(iconUrl + "&ti=true&ext=.png");


        return icon;
    };

}
