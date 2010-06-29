package org.sigmah.shared.command;

import org.sigmah.shared.command.result.CreateResult;


/**
 *
 * Creates a new Report Definition
 *
 * Returns {@link org.sigmah.shared.command.result.CreateResult}
 *
 * @author Alex Bertram
 */
public class CreateReportDef implements Command<CreateResult> {
	
	private String xml;
	private Integer databaseId;
	
	protected CreateReportDef() {
		
	}

	public CreateReportDef(int databaseId, String xml) {
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
