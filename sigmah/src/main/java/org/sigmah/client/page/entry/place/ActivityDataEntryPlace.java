/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.place;


import org.sigmah.client.page.PageId;
import org.sigmah.client.page.entry.DataEntryPage;
import org.sigmah.client.page.entry.SitePageState;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.dto.ActivityDTO;


public class ActivityDataEntryPlace extends DataEntryPlace implements SitePageState {

	public static final String TOKEN = "activity";
	
    int activityId;
    
    public ActivityDataEntryPlace() {
    	activityId = 0;
    }

    public ActivityDataEntryPlace(ActivityDataEntryPlace place) {
        this.activityId = place.activityId;
        this.pageNum = place.pageNum;
        this.sortInfo = place.sortInfo;
    }
   
    public ActivityDataEntryPlace(ActivityDTO activity) {
        this.activityId = activity.getId();
    }

    public ActivityDataEntryPlace(int activityId) {
        this.activityId = activityId;
    }

    @Override
	public PageId getPageId() {
		return DataEntryPage.PAGE_ID;
	}

    @Override
    public String serializeAsHistoryToken() {
        StringBuilder sb = new StringBuilder();
        sb.append(TOKEN);
        sb.append("/");
        sb.append(activityId);
        appendGridStateToken(sb);
        return sb.toString();
    }

    public int getActivityId() {
        return activityId;
    }

	public ActivityDataEntryPlace setActivityId(int activityId) {
        this.activityId = activityId;
        return this;
    }
	
    @Override
	public Filter getFilter() {
		return Filter.filter().onActivity(activityId);
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivityDataEntryPlace that = (ActivityDataEntryPlace) o;

        if (activityId != that.activityId) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return activityId;
    }
}
