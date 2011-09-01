package org.sigmah.client.page.dashboard;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;
import org.sigmah.client.page.config.AccountPageState;

public class DashboardPageState implements PageState {

    @Override
    public String serializeAsHistoryToken() {
        return null;
    }

	@Override
	public List<PageId> getEnclosingFrames() {
		return Arrays.asList(Dashboard.DASHBOARD);
	}

	@Override
	public PageId getPageId() {
		return Dashboard.DASHBOARD;
	}

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof AccountPageState;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public static class Parser implements PageStateParser {
        @Override
        public PageState parse(String token) {
            return new DashboardPageState();
        }
    }

}