package org.activityinfo.client.page.report;

import org.activityinfo.client.Place;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.Pages;

import java.util.List;

/**
 * @author Alex Bertram
 */
public class ReportHomePlace implements Place {

    public PageId getPageId() {
        return Pages.ReportHome;
    }

    public String pageStateToken() {
        return null;
    }

    public List<ViewPath.Node> getViewPath() {
        return ViewPath.make(Pages.ReportHome);
    }
}
