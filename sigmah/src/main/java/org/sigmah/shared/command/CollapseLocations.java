package org.sigmah.shared.command;

import java.util.List;

import org.sigmah.shared.command.CollapseLocations.CollapseLocationsResult;
import org.sigmah.shared.command.result.CommandResult;

public class CollapseLocations implements Command<CollapseLocationsResult>{
	private int masterLocationId;
	private List<Integer> locationsToCollapse;
	
	public CollapseLocations(int masterLocationId, List<Integer> locationsToCollapse) {
		this.masterLocationId = masterLocationId;
		this.locationsToCollapse = locationsToCollapse;
	}

	public int getMasterLocationId() {
		return masterLocationId;
	}

	public List<Integer> getLocationsToCollapse() {
		return locationsToCollapse;
	}

	public class CollapseLocationsResult implements CommandResult {
		private boolean isSuccess = false;

		public boolean isSuccess() {
			return isSuccess;
		}

		public void setSuccess(boolean isSuccess) {
			this.isSuccess = isSuccess;
		}
		
	}

}
