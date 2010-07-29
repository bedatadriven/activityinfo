/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.page.dashboard;

import java.util.Arrays;
import java.util.List;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;

/**
 *
 * @author rca
 */
public class DashboardPageState implements PageState {
    public static class Parser implements PageStateParser {
        private final static DashboardPageState instance = new DashboardPageState();
        
        @Override
        public PageState parse(String token) {
            return instance;
        }
    }

    @Override
    public String serializeAsHistoryToken() {
        return null;
    }

    @Override
    public PageId getPageId() {
        return DashboardPresenter.PAGE_ID;
    }

    @Override
    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(DashboardPresenter.PAGE_ID);
    }
}
