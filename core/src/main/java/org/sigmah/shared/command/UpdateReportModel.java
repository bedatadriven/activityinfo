package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.report.model.Report;

public class UpdateReportModel implements Command<VoidResult>{

	private Report model;
	
	public UpdateReportModel() {
		
	}

	public Report getModel() {
		return model;
	}

	public void setModel(Report model) {
		this.model = model;
	}

}
