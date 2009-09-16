package org.activityinfo.shared.dto;

import org.activityinfo.shared.command.result.CommandResult;

import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteMarkerCollection implements CommandResult {

    private List<SiteMarker> sites;

    private SiteMarkerCollection() {
        
    }

    public SiteMarkerCollection(List<SiteMarker> sites) {
        this.sites = sites;
    }

    public List<SiteMarker> getSites() {
        return sites;
    }

    public void setSites(List<SiteMarker> sites) {
        this.sites = sites;
    }
}
