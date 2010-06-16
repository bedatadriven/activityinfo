package org.activityinfo.client.page.config;

import org.activityinfo.client.page.Frames;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.PageStateParser;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class AccountPageState implements PageState {


    @Override
    public String serializeAsHistoryToken() {
        return null;
    }

	@Override
	public List<PageId> getEnclosingFrames() {
		return Arrays.asList(Frames.ConfigFrameSet, AccountEditor.Account);
	}

	@Override
	public PageId getPageId() {
		return AccountEditor.Account;
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
            return new AccountPageState();
        }
    }
}
