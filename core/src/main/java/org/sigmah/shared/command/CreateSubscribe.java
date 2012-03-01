package org.sigmah.shared.command;

import java.util.List;

import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.report.model.ReportFrequency;
import org.sigmah.shared.report.model.ReportSubscriber;

public class CreateSubscribe implements Command<VoidResult> {

	private int reportTemplateId;
	private List<ReportSubscriber> emailsList;
	private int day;
	private ReportFrequency reportFrequency;
	
	public CreateSubscribe() {
	}

	public int getReportTemplateId() {
		return reportTemplateId;
	}

	public void setReportTemplateId(int reportTemplateId) {
		this.reportTemplateId = reportTemplateId;
	}

	public List<ReportSubscriber> getEmailsList() {
		return emailsList;
	}

	public void setEmailsList(List<ReportSubscriber> emailsList) {
		this.emailsList = emailsList;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public ReportFrequency getReportFrequency() {
		return reportFrequency;
	}

	public void setReportFrequency(ReportFrequency reportFrequency) {
		this.reportFrequency = reportFrequency;
	}
	
}
