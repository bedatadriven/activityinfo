/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.config;

import org.sigmah.client.page.Frames;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;

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
