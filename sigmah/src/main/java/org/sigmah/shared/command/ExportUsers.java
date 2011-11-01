package org.sigmah.shared.command;

import org.sigmah.shared.command.result.RenderResult;

public class ExportUsers implements Command<RenderResult> {
	private int databaseId;
	private boolean showPermissions = true;
	
	public ExportUsers setShowPermissions(boolean showPermissions) {
		this.showPermissions = showPermissions;
		return this;
	}
	public boolean isShowPermissions() {
		return showPermissions;
	}
	public ExportUsers setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
		return this;
	}
	public int getDatabaseId() {
		return databaseId;
	}
	
}
