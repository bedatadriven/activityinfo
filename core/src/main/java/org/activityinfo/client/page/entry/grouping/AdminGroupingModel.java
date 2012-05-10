package org.activityinfo.client.page.entry.grouping;


public class AdminGroupingModel extends GroupingModel {

	private final int adminLevelId;

	public AdminGroupingModel(int adminLevelId) {
		super();
		this.adminLevelId = adminLevelId;
	}

	public int getAdminLevelId() {
		return adminLevelId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + adminLevelId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AdminGroupingModel other = (AdminGroupingModel) obj;
		if (adminLevelId != other.adminLevelId) {
			return false;
		}
		return true;
	}	
	
	
}
