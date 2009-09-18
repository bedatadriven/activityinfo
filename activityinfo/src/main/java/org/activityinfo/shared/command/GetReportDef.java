package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.SingleResult;
import org.activityinfo.shared.command.result.XmlResult;

public class GetReportDef implements Command<XmlResult> {


	private int id;
	
	protected GetReportDef() {
		
	}

	public GetReportDef(int reportId) {
		id = reportId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
