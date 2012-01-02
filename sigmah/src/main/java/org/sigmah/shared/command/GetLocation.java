package org.sigmah.shared.command;

import org.sigmah.shared.dto.LocationDTO;

public class GetLocation implements Command<LocationDTO> {
	private int locationId;

	public GetLocation() {
		
	}
	
	public GetLocation(int id) {
		this.locationId = id;
	}
	
	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
}
