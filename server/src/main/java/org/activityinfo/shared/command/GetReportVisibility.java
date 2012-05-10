package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.ReportVisibilityResult;

public class GetReportVisibility implements Command<ReportVisibilityResult> {
	private int reportId;

	public GetReportVisibility() {
		super();
	}

	public GetReportVisibility(int reportId) {
		super();
		this.reportId = reportId;
	}

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

}
