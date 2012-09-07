package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.report.model.Report;

public class UpdateReportModel implements MutatingCommand<VoidResult>{

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
