package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.dto.SiteMarker;
import org.activityinfo.shared.dto.SiteMarkerCollection;

import java.util.List;

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
