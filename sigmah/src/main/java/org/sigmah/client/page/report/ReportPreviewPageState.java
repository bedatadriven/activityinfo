/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.report;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;

import java.util.Arrays;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class ReportPreviewPageState implements PageState {

    private int reportId;

    public ReportPreviewPageState(int reportId) {
        this.reportId = reportId;
    }

    public PageId getPageId() {
        return ReportPreviewPresenter.ReportPreview;
    }

    public String serializeAsHistoryToken() {
        return Integer.toString(reportId);
    }

    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(ReportPreviewPresenter.ReportPreview);
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public static class Parser implements PageStateParser {

        public PageState parse(String token) {
            return new ReportPreviewPageState(Integer.parseInt(token));
        }
    }
}
