package org.sigmah.shared.command;

import org.sigmah.shared.report.model.Report;

/** 
 * Retrieves a report model from the server
 *
 */
public class GetReportModel implements Command<Report> {
	
	private int reportId;
	
	public GetReportModel(){
		
	}

	public GetReportModel(int reportId){
		this.reportId = reportId;
	}
	
	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}
	
}
