package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

public class DeleteReport implements Command<VoidResult> {
	private int reportId;

	public DeleteReport() {
		
	}
	
	public DeleteReport(int id) {
		this.reportId = id;
	}

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}
}
