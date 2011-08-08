package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.LockedPeriodDTO;

public class UpdateLockedPeriod implements Command<VoidResult> {
	private int activityId;
	private int userDatabaseId;
	private int projectId;
	
	public UpdateLockedPeriod(LockedPeriodDTO lockedPeriod) {
		// TODO Auto-generated constructor stub
	}
	
	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getUserDatabaseId() {
		return userDatabaseId;
	}

	public void setUserDatabaseId(int userDatabaseId) {
		this.userDatabaseId = userDatabaseId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
}
