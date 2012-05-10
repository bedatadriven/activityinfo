package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.LockedPeriodDTO;

public class LockEntity implements Command<CreateResult> {
	private int activityId=0;
	private int userDatabseId=0;
	private int projectId=0;
	private LockedPeriodDTO lockedPeriod;
	
	public LockEntity() {
	}

	public LockEntity(LockedPeriodDTO lockedPeriod) {
		this.setLockedPeriod(lockedPeriod);
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getUserDatabseId() {
		return userDatabseId;
	}

	public void setUserDatabaseId(int userDatabseId) {
		this.userDatabseId = userDatabseId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public void setLockedPeriod(LockedPeriodDTO lockedPeriod) {
		this.lockedPeriod = lockedPeriod;
	}

	public LockedPeriodDTO getLockedPeriod() {
		return lockedPeriod;
	}
}