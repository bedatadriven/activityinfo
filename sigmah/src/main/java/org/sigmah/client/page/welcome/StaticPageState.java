/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.welcome;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.page.Frames;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;
import org.sigmah.client.page.app.Section;

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
