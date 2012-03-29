package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ReportVisibilityDTO extends BaseModelData {
	
	private int databaseId;

	public int getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}
	public String getDatabaseName() {
		return get("databaseName");
	}
	public void setDatabaseName(String databaseName) {
		set("databaseName", databaseName);
	}
	public boolean isVisible() {
		return (Boolean)get("visible");
	}
	public void setVisible(boolean visible) {
		set("visible", visible);
	}
	public boolean isDefaultDashboard() {
		return (Boolean)get("defaultDashboard", false);
	}
	
	public void setDefaultDashboard(boolean defaultDashboard) {
		set("defaultDashboard", defaultDashboard);
	}
	
	
	

}
