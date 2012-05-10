package org.sigmah.server.command;

import org.activityinfo.client.offline.command.handler.KeyGenerator;
import org.activityinfo.shared.dto.LocationDTO;

public class LocationDTOs {
	public static LocationDTO newLocation() {
		return new LocationDTO()
			.setId(new KeyGenerator().generateInt())
			.setName("Virunga")
			.setAxe("Goma - Rutshuru")
			.setLongitude(27.432)
			.setLatitude(1.23)
			.setLocationTypeId(1);
	}
}
