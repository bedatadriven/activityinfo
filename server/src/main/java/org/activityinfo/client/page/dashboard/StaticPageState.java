/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.dashboard;

import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.page.Frames;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.PageStateParser;
import org.activityinfo.client.page.app.Section;

public final class StaticPageState implements PageState {

    private String keyword;

    public StaticPageState(String keyword) {
        this.keyword = keyword;
    }

    @Override
	public PageId getPageId() {
        return Frames.STATIC;
    }

    @Override
	public String serializeAsHistoryToken() {
        return keyword;
    }

    @Override
	public List<PageId> getEnclosingFrames() {
        return Arrays.asList(Frames.STATIC);
    }

    public String getKeyword() {
        return keyword;
    }
    
    public static class Parser implements PageStateParser {

        @Override
		public PageState parse(String token) {
            return new StaticPageState(token);
        }
    }

	@Override
	public Section getSection() {
		return Section.HOME;
	}


}
