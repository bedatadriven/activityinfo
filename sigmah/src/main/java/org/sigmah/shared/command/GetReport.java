package org.sigmah.shared.command;

import org.sigmah.shared.command.result.ReportTemplateResult;

public class GetReport implements Command<ReportTemplateResult> {
	
	private int reportTemplateId;
	
	public GetReport(){
		
	}

	public GetReport(int reportTemplateId){
		this.reportTemplateId = reportTemplateId;
	}
	
	public int getReportTemplateId() {
		return reportTemplateId;
	}

	public void setReportTemplateId(int reportTemplateId) {
		this.reportTemplateId = reportTemplateId;
	}
	
}
