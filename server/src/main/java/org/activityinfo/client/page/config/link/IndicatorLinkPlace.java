package org.activityinfo.client.page.config.link;

import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.app.Section;
import org.activityinfo.client.page.config.ConfigFrameSet;
import org.activityinfo.client.page.config.DbListPageState;

public class IndicatorLinkPlace implements PageState {

	@Override
	public String serializeAsHistoryToken() {
		return null;
	}

	@Override
	public List<PageId> getEnclosingFrames() {
		return Arrays.asList(ConfigFrameSet.PAGE_ID, IndicatorLinkPage.PAGE_ID);
	}

	@Override
	public PageId getPageId() {
		return IndicatorLinkPage.PAGE_ID;
	}

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof DbListPageState;
    }

    @Override
    public int hashCode() {
        return 0;
    }

	@Override
	public Section getSection() {
		return Section.DESIGN;
	}

}
