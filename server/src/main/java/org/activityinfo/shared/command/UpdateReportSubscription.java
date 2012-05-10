package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.report.model.EmailDelivery;

/**
 * Updates the relationship between a user and a report
 *
 */
public class UpdateReportSubscription implements Command<VoidResult>{
	private int reportId;
	private String userEmail;
	
	private Boolean pinnedToDashboard;
	private Integer emailDay;
	private EmailDelivery frequency;

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Boolean getPinnedToDashboard() {
		return pinnedToDashboard;
	}

	public void setPinnedToDashboard(Boolean pinnedToDashboard) {
		this.pinnedToDashboard = pinnedToDashboard;
	}


	public Integer getEmailDay() {
		return emailDay;
	}

	public void setEmailDay(Integer day) {
		this.emailDay = day;
	}

	public EmailDelivery getEmailDelivery() {
		return frequency;
	}

	public void setEmailDelivery(EmailDelivery frequency) {
		this.frequency = frequency;
	}
}
