package org.sigmah.shared.command;

import org.sigmah.shared.command.result.SitePointList;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class GetSitePoints implements Command<SitePointList> {

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


