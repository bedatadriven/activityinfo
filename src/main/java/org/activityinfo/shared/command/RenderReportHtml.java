/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.HtmlResult;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.ReportElement;

public class RenderReportHtml implements Command<HtmlResult> {

    private ReportElement model;


    public RenderReportHtml() {
    }


	public RenderReportHtml(ReportElement model) {
		super();
		this.model = model;
	}

	public ReportElement getModel() {
		return model;
	}


	public void setModel(ReportElement model) {
		this.model = model;
	}


}
