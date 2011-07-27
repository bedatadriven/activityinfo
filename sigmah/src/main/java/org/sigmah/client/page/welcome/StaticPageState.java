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

public class StaticPageState implements PageState {

    String keyword;

    public StaticPageState(String keyword) {
        this.keyword = keyword;
    }

    public PageId getPageId() {
        return Frames.Static;
    }

    public String serializeAsHistoryToken() {
        return keyword;
    }

    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(Frames.Static);
    }

    public String getKeyword() {
        return keyword;
    }
    
    public static class Parser implements PageStateParser {

        public PageState parse(String token) {
            return new StaticPageState(token);
        }
    }


}
