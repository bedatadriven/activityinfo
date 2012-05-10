/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.SitePointList;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class GetSitePoints implements Command<SitePointList> {

    private int activityId  ;

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


