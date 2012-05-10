/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.config;

import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.app.Section;

public class DbListPageState implements PageState {

    public DbListPageState() {
        
    }

    @Override
    public String serializeAsHistoryToken() {
        return null;
    }

	@Override
	public List<PageId> getEnclosingFrames() {
		return Arrays.asList(ConfigFrameSet.PAGE_ID, DbListPresenter.PAGE_ID);
	}

	@Override
	public PageId getPageId() {
		return DbListPresenter.PAGE_ID;
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