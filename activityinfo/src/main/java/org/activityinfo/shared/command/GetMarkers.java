package org.activityinfo.shared.command;

import org.activityinfo.shared.dto.SiteMarkerCollection;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class GetMarkers implements Command<SiteMarkerCollection> {

    private int zoomLevel;

    private GetMarkers() {
        
    }

    public GetMarkers(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }
}
