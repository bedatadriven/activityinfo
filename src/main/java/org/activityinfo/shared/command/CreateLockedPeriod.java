package org.activityinfo.shared.command;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.LockedPeriodDTO;

public class CreateLockedPeriod implements MutatingCommand<CreateResult> {
	private int activityId=0;
	private int userDatabseId=0;
	private int projectId=0;
	private LockedPeriodDTO lockedPeriod;
	
	public CreateLockedPeriod() {
	}

	public CreateLockedPeriod(LockedPeriodDTO lockedPeriod) {
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