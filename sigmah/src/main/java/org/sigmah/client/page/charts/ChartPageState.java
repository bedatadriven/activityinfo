package org.sigmah.client.page.charts;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;

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
