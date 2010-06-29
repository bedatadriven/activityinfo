package org.sigmah.client.page.welcome;

import org.sigmah.client.page.Frames;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;

import java.util.Arrays;
import java.util.List;
/*
 * @author Alex Bertram
 */

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
