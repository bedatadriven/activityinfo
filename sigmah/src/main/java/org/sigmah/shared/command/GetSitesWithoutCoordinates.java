package org.sigmah.shared.command;

import org.sigmah.shared.command.result.SitesWithoutLocationsResult;

public class GetSitesWithoutCoordinates implements Command<SitesWithoutLocationsResult>{
	private int maxLocations = 10;

	public GetSitesWithoutCoordinates() {
	}

	public GetSitesWithoutCoordinates setMaxLocations(int maxLocations) {
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
