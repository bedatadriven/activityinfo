package org.activityinfo.client.page.config;

import org.activityinfo.client.Place;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.Pages;

import java.util.List;

public class DbListPlace implements Place {

    public DbListPlace() {
        
    }

    @Override
    public String pageStateToken() {
        return null;
    }

	@Override
	public List<ViewPath.Node> getViewPath() {
		return ViewPath.make(Pages.ConfigFrameSet, Pages.DatabaseList);
	}

	@Override
	public PageId getPageId() {
		return Pages.DatabaseList;
	}

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof DbListPlace;
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
