package org.sigmah.shared.command;

import java.util.List;

import org.sigmah.shared.command.GetLockedPeriods.LockedPeriodsResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.LockedPeriodDTO;

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
