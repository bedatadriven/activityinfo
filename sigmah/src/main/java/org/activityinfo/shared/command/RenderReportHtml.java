package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.HtmlResult;
import org.activityinfo.shared.report.model.DateRange;

public class RenderReportHtml implements Command<HtmlResult> {

    private int templateId;
    private DateRange dateRange;


    public RenderReportHtml() {
    }

    public RenderReportHtml(int templateId, DateRange dateRange) {
        this.templateId = templateId;
        this.dateRange = dateRange;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }
}
