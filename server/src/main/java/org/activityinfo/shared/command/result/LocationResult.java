package org.activityinfo.shared.command.result;

import java.util.List;

import org.activityinfo.shared.dto.LocationDTO;

public class LocationResult extends PagingResult<LocationDTO> {
	
	public LocationResult() {
		super();
	}

	public LocationResult(List<LocationDTO> data) {
		super(data);
	}
}