/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.dashboard;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.app.Section;

public class DashboardPlace implements PageState {

    @Override
	public PageId getPageId() {
        return DashboardPage.PAGE_ID;
    }

    @Override
	public String serializeAsHistoryToken() {
        return null;
    }

    @Override
	public List<PageId> getEnclosingFrames() {
        return Arrays.asList(DashboardPage.PAGE_ID);
    }

	@Override
	public Section getSection() {
		return Section.HOME;
	}
    
}