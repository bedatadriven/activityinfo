package org.activityinfo.shared.command;

import java.util.List;

import org.activityinfo.shared.command.GetLockedPeriods.LockedPeriodsResult;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.LockedPeriodDTO;

public class GetLockedPeriods implements Command<LockedPeriodsResult> {
	private int databaseId;
	
	public GetLockedPeriods(int databaseId) {
		super();
		this.databaseId = databaseId;
	}

	public int getDatabaseId() {
		return databaseId;
	}

	public static class LockedPeriodsResult implements CommandResult {
		private List<LockedPeriodDTO> lockedPeriods;

		public LockedPeriodsResult(List<LockedPeriodDTO> lockedPeriods) {
			this.lockedPeriods = lockedPeriods;
		}

		public List<LockedPeriodDTO> getLockedPeriods() {
			return lockedPeriods;
		}
	}
}
