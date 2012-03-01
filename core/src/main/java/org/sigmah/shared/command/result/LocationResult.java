package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.LocationDTO;

public class LocationResult extends PagingResult<LocationDTO> {
	
	public LocationResult() {
		super();
	}

	public LocationResult(List<LocationDTO> data) {
		super(data);
	}
}