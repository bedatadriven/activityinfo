package org.sigmah.shared.command;

import org.sigmah.shared.command.result.LocationResult;

public class GetLocationsWithoutGpsCoordinates implements Command<LocationResult> {
	private int maxLocations = 10;

	public GetLocationsWithoutGpsCoordinates setMaxLocations(int maxLocations) {
		this.maxLocations = maxLocations;
		return this;
	}

	public int getMaxLocations() {
		return maxLocations;
	}
	
	public boolean hasLocationCountLimit() {
		return maxLocations != 0;
	}
}
