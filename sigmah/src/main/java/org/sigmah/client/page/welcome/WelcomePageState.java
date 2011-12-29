/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.welcome;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

public class WelcomePageState implements PageState {

    @Override
	public PageId getPageId() {
        return Welcome.PAGE_ID;
    }

    @Override
	public String serializeAsHistoryToken() {
        return null;
    }

    @Override
	public List<PageId> getEnclosingFrames() {
        return Arrays.asList(Welcome.PAGE_ID);
    }
    
}