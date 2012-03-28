package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

public class UpdateReportModel implements Command<VoidResult>{

	private String reportJsonModel;
	
	public UpdateReportModel() {
		
	}

	public String getReportJsonModel() {
		return reportJsonModel;
	}

	public void setReportJsonModel(String reportJsonModel) {
		this.reportJsonModel = reportJsonModel;
	}

}
