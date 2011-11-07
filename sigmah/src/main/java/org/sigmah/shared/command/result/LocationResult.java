package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.LocationDTO;

public class LocationResult extends PagingResult<LocationDTO> {

	private int totalLocationsCount = 0;
	
	public LocationResult() {
		super();
	}

	public LocationResult(List<LocationDTO> data) {
		super(data);
	}

	public void setData(List<LocationDTO> locations) {
		data=locations;
	}
}