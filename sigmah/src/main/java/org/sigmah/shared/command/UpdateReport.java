package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.report.model.Report;

public class UpdateReport implements Command<VoidResult>{

	private int id;
	private Report model;
	
	public UpdateReport(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Report getModel() {
		return model;
	}

	public void setModel(Report model) {
		this.model = model;
	}
}
