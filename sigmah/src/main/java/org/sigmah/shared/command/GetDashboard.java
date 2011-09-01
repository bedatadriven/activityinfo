package org.sigmah.shared.command;

import org.sigmah.shared.command.result.DashboardSettingsResult;

/*
 *Request the dashboard of the user with given UserID from a persistence store
 */
public class GetDashboard implements Command<DashboardSettingsResult> {
	private int userId;

	public GetDashboard(int userId) {
		super();
		this.userId = userId;
	}

	public GetDashboard() {
		super();
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}
}