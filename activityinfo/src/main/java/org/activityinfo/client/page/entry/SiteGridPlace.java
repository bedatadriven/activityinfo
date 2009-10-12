package org.activityinfo.client.page.entry;

import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceParser;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.common.grid.AbstractPagingGridPlace;
import org.activityinfo.shared.dto.ActivityModel;

import java.util.List;


public class SiteGridPlace extends AbstractPagingGridPlace {

    private int activityId;

    public SiteGridPlace() {
    	activityId = 0;
    }

    public SiteGridPlace(SiteGridPlace place) {
        this.activityId = place.activityId;
        this.pageNum = place.pageNum;
        this.sortInfo = place.sortInfo;
    }
   
    public SiteGridPlace(ActivityModel activity) {
        this.activityId = activity.getId();
    }

    public SiteGridPlace(int activityId) {
        this.activityId = activityId;
    }

    @Override
	public PageId getPageId() {
		return Pages.SiteGrid;
	}

    @Override
    public String pageStateToken() {
        StringBuilder sb = new StringBuilder();
        sb.append(activityId);
        appendGridStateToken(sb);
        return sb.toString();
    }

    public int getActivityId() {
        return activityId;
    }

	@Override
	public List<ViewPath.Node> getViewPath() {
		return ViewPath.make(
				new ViewPath.Node(ViewPath.DefaultRegion, Pages.DataEntryFrameSet),
				new ViewPath.Node(ViewPath.DefaultRegion, Pages.SiteGrid));
	}

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiteGridPlace that = (SiteGridPlace) o;

        if (activityId != that.activityId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return activityId;
    }

    public static class Parser implements PlaceParser {

        @Override
        public Place parse(String token) {

            SiteGridPlace place = new SiteGridPlace();

            for(String t : token.split("/")) {

                if(!place.parseGridStateTokens(t)) {
                    place.activityId = Integer.parseInt(t);
                }
            }
            return place;
        }

    }
}
