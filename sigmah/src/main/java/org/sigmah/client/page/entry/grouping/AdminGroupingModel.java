package org.sigmah.client.page.entry.grouping;


public class AdminGroupingModel extends GroupingModel {

	private final int adminLevelId;

	public AdminGroupingModel(int adminLevelId) {
		super();
		this.adminLevelId = adminLevelId;
	}

	public int getAdminLevelId() {
		return adminLevelId;
	}	
}
