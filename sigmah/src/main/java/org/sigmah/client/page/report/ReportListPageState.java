/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.report;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

import java.util.Arrays;
import java.util.List;

/**
 * Page State object
 *
 * @author Alex Bertram
 */
public class ReportListPageState implements PageState {

    public PageId getPageId() {
        return ReportListPagePresenter.ReportHome;
    }

    public String serializeAsHistoryToken() {
        return null;
    }

    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(ReportListPagePresenter.ReportHome);
    }
}
