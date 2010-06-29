/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.config;

import org.sigmah.client.page.Frames;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

import java.util.Arrays;
import java.util.List;

public class DbListPageState implements PageState {

    public DbListPageState() {
        
    }

    @Override
    public String serializeAsHistoryToken() {
        return null;
    }

	@Override
	public List<PageId> getEnclosingFrames() {
		return Arrays.asList(Frames.ConfigFrameSet, DbListPresenter.DatabaseList);
	}

	@Override
	public PageId getPageId() {
		return DbListPresenter.DatabaseList;
	}

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof DbListPageState;
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
