package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

/**
 * Updates the relationship between a user and a report
 *
 */
public class UpdateReportSubscription implements Command<VoidResult>{
	private int reportId;
	private String userEmail;
	
	private Boolean pinnedToDashboard;

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
	
	
	
}
