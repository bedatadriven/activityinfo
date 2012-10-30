package org.activityinfo.shared.command;

import java.util.Set;

import org.activityinfo.shared.command.result.AdminLevelResult;

public class GetAdminLevels implements Command<AdminLevelResult>{
	private Set<Integer> indicatorIds;

	public Set<Integer> getIndicatorIds() {
		return indicatorIds;
	}

	public void setIndicatorIds(Set<Integer> indicatorIds) {
		this.indicatorIds = indicatorIds;
	}
	
	
}
