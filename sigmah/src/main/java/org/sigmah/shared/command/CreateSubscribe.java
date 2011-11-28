package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

public class CreateSubscribe implements Command<VoidResult> {

	int reportTemplateId;
	private String emailsList;
	
	public CreateSubscribe() {
	}

	public int getReportTemplateId() {
		return reportTemplateId;
	}

	public void setReportTemplateId(int reportTemplateId) {
		this.reportTemplateId = reportTemplateId;
	}

	public String getEmailsList() {
		return emailsList;
	}

	public void setEmailsList(String emailsList) {
		this.emailsList = emailsList;
	}
	
}
