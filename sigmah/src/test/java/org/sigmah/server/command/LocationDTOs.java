package org.sigmah.server.command;

import org.sigmah.shared.dto.LocationDTO;

public class LocationDTOs {
	public static LocationDTO newLocation() {
		return new LocationDTO()
			.setId(1)
			.setName("Virunga")
			.setAxe("Goma - Rutshuru")
			.setLongitude(27.432)
			.setLatitude(1.23)
			.setLocationTypeId(1);
	}
}
