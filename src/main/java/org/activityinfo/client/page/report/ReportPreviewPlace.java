package org.activityinfo.client.page.report;

import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceParser;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.Pages;

import java.util.List;
/*
 * @author Alex Bertram
 */

public class ReportPreviewPlace implements Place {

    private int reportId;

    public ReportPreviewPlace(int reportId) {
        this.reportId = reportId;
    }

    public PageId getPageId() {
        return Pages.ReportPreview;
    }

    public String pageStateToken() {
        return Integer.toString(reportId);
    }

    public List<ViewPath.Node> getViewPath() {
        return ViewPath.make(Pages.ReportPreview);
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public static class Parser implements PlaceParser {

        public Place parse(String token) {
            return new ReportPreviewPlace(Integer.parseInt(token));
        }
    }
}
