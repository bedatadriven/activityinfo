package org.activityinfo.shared.command;

import org.activityinfo.shared.dto.SitePointCollection;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class GetSitePoints implements Command<SitePointCollection> {

    private int activityId;

    private GetSitePoints() {

    }

    public GetSitePoints(int activityId) {
        this.activityId= activityId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }
}


