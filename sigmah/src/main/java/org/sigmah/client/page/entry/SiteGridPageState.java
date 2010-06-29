/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import org.sigmah.client.page.Frames;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;
import org.sigmah.client.page.common.grid.AbstractPagingGridPageState;
import org.sigmah.shared.dto.ActivityDTO;

import java.util.Arrays;
import java.util.List;


public class SiteGridPageState extends AbstractPagingGridPageState {

    private int activityId;

    public SiteGridPageState() {
    	activityId = 0;
    }

    public SiteGridPageState(SiteGridPageState place) {
        this.activityId = place.activityId;
        this.pageNum = place.pageNum;
        this.sortInfo = place.sortInfo;
    }
   
    public SiteGridPageState(ActivityDTO activity) {
        this.activityId = activity.getId();
    }

    public SiteGridPageState(int activityId) {
        this.activityId = activityId;
    }

    @Override
	public PageId getPageId() {
		return SiteEditor.ID;
	}

    @Override
    public String serializeAsHistoryToken() {
        StringBuilder sb = new StringBuilder();
        sb.append(activityId);
        appendGridStateToken(sb);
        return sb.toString();
    }

    public int getActivityId() {
        return activityId;
    }

	@Override
	public List<PageId> getEnclosingFrames() {
		return Arrays.asList(Frames.DataEntryFrameSet,SiteEditor.ID);
	}

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SiteGridPageState that = (SiteGridPageState) o;

        if (activityId != that.activityId) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return activityId;
    }
    

    public static class Parser implements PageStateParser {

        @Override
        public PageState parse(String token) {

            SiteGridPageState place = new SiteGridPageState();

            for(String t : token.split("/")) {

                if(!place.parseGridStateTokens(t)) {
                    place.activityId = Integer.parseInt(t);
                }
            }
            return place;
        }

    }
}
