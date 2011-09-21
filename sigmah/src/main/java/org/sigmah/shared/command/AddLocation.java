package org.sigmah.shared.command;

import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.LocationDTO2;

public class AddLocation implements Command<CreateResult> {
	private LocationDTO2 location;

	public AddLocation setLocation(LocationDTO2 location) {
		this.location = location;
		return this;
	}

	public LocationDTO2 getLocation() {
		return location;
	}
	
}
