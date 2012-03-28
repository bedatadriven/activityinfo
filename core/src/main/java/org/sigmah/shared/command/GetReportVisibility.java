package org.sigmah.shared.command;

import org.sigmah.shared.command.result.ReportVisibilityResult;

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
