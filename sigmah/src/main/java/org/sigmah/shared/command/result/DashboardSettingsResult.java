package org.sigmah.shared.command.result;

import org.sigmah.shared.dto.DashboardSettingsDTO;

public class DashboardSettingsResult implements CommandResult {
	private DashboardSettingsDTO dashboard;

	public DashboardSettingsResult(DashboardSettingsDTO settings) {
		this.dashboard=settings;
	}

	public void setDashboard(DashboardSettingsDTO dashboard) {
		this.dashboard = dashboard;
	}

	public DashboardSettingsDTO getDashboard() {
		return dashboard;
	}
}
