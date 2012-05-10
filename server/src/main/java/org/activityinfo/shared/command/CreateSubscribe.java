package org.activityinfo.shared.command;

import java.util.List;

import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.report.model.EmailDelivery;
import org.activityinfo.shared.report.model.ReportSubscriber;

public class CreateSubscribe implements Command<VoidResult> {

	private int reportTemplateId;
	private List<ReportSubscriber> emailsList;
	private int day;
	private EmailDelivery reportFrequency;
	
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

	public EmailDelivery getReportFrequency() {
		return reportFrequency;
	}

	public void setReportFrequency(EmailDelivery reportFrequency) {
		this.reportFrequency = reportFrequency;
	}
	
}
