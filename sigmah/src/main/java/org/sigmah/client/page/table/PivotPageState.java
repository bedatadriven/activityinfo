/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.table;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;
import org.sigmah.client.page.app.Section;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotPageState implements PageState {

    @Override
    public PageId getPageId() {
        return PivotPresenter.PAGE_ID;
    }

    @Override
    public String serializeAsHistoryToken() {
        return null;
    }

    @Override
    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(PivotPresenter.PAGE_ID);
    }

    public static class Parser implements PageStateParser {
        @Override
        public PageState parse(String token) {
            return new PivotPageState();
        }
    }

	@Override
	public Section getSection() {
		return Section.ANALYSIS;
	}
}
