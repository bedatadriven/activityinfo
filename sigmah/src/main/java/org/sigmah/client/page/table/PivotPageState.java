package org.sigmah.client.page.table;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotPageState implements PageState {

    @Override
    public PageId getPageId() {
        return PivotPresenter.Pivot;
    }

    @Override
    public String serializeAsHistoryToken() {
        return null;
    }

    @Override
    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(PivotPresenter.Pivot);
    }

    public static class Parser implements PageStateParser {
        @Override
        public PageState parse(String token) {
            return new PivotPageState();
        }
    }
}
