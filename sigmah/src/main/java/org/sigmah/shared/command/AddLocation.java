package org.sigmah.shared.command;

import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.LocationDTO;

public class AddLocation implements Command<CreateResult> {
	private LocationDTO location;

	public AddLocation setLocation(LocationDTO location) {
		this.location = location;
		return this;
	}

	public LocationDTO getLocation() {
		return location;
	}
	
}
