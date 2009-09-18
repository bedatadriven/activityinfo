package org.activityinfo.client.page.config;

import java.util.List;

import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceParser;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.Pages;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class AccountPlace implements Place {


    @Override
    public String pageStateToken() {
        return null;
    }

	@Override
	public List<ViewPath.Node> getViewPath() {
		return ViewPath.make(
				new ViewPath.Node(ViewPath.DefaultRegion, Pages.ConfigFrameSet),
				new ViewPath.Node(ViewPath.DefaultRegion, Pages.Account) );
		
	}

	@Override
	public PageId getPageId() {
		return Pages.Account;
	}

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof AccountPlace;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public static class Parser implements PlaceParser {
        @Override
        public Place parse(String token) {
            return new AccountPlace();
        }
    }
}
