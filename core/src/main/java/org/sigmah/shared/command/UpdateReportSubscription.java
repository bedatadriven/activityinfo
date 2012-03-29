package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.report.model.ReportFrequency;

/**
 * Updates the relationship between a user and a report
 *
 */
public class UpdateReportSubscription implements Command<VoidResult>{
	private int reportId;
	private String userEmail;
	
	private Boolean pinnedToDashboard;
	private Boolean subscribed;
	private Integer day;
	private ReportFrequency frequency;

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

	public Boolean getSubscribed() {
		return subscribed;
	}

	public void setSubscribed(Boolean subscribed) {
		this.subscribed = subscribed;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public ReportFrequency getFrequency() {
		return frequency;
	}

	public void setFrequency(ReportFrequency frequency) {
		this.frequency = frequency;
	}
	
	
	
}
