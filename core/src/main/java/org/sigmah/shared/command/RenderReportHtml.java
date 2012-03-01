/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.command.result.HtmlResult;
import org.sigmah.shared.report.model.DateRange;

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
