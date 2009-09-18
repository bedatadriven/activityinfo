package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.CreateResult;

public class CreateReportDef implements Command<CreateResult> {
	
	private String xml;
	private Integer databaseId;
	
	protected CreateReportDef() {
		
	}

	public CreateReportDef(Integer databaseId, String xml) {
		super();
		this.databaseId = databaseId;
		this.xml = xml;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public Integer getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(Integer databaseId) {
		this.databaseId = databaseId;
	}

}
