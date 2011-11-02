package org.sigmah.server.command;

import org.sigmah.shared.dto.LocationDTO2;

public class LocationDTOs {
	public static LocationDTO2 newLocation() {
		return new LocationDTO2()
			.setId(1)
			.setName("Virunga")
			.setAxe("Goma - Rutshuru")
			.setLongitude(27.432)
			.setLatitude(1.23)
			.setLocationTypeId(1);
	}
}
