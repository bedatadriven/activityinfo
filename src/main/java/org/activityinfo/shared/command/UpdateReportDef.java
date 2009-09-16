package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.VoidResult;

public class UpdateReportDef implements Command<VoidResult> {

	private int id;
	private String newXml;
	
	public UpdateReportDef() {
	
	}
	
	public UpdateReportDef(int id, String newXml) {
		this.id = id;
		this.newXml = newXml;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNewXml() {
		return newXml;
	}

	public void setNewXml(String newXml) {
		this.newXml = newXml;
	}
	

}
