package org.activityinfo.client.page.charts;

import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.PageStateParser;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ChartPageState implements PageState {

    public String serializeAsHistoryToken() {
        return null;
    }

    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(getPageId());
    }

    @Override
    public PageId getPageId() {
        return Charts.Charts;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof ChartPageState;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public static class Parser implements PageStateParser {

        @Override
        public PageState parse(String token) {
            return new ChartPageState();
        }
    }
}
