package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.LocationDTO;

public class LocationsWithoutGpsResult extends ListResult<LocationDTO> {

	public LocationsWithoutGpsResult(List<LocationDTO> data) {
		super(data);
	}

	public LocationsWithoutGpsResult(ListResult<LocationDTO> result) {
		super(result);
	}
}