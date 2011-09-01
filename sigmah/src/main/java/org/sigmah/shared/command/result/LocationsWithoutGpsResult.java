package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.LocationDTO;

public class LocationsWithoutGpsResult extends ListResult<LocationDTO> {

	private int totalLocationsCount = 0;
	
	public LocationsWithoutGpsResult() {
		super();
	}

	public LocationsWithoutGpsResult(List<LocationDTO> data) {
		super(data);
	}

	public LocationsWithoutGpsResult(ListResult<LocationDTO> result) {
		super(result);
	}

	public void setData(List<LocationDTO> locations) {
		data=locations;
	}

	public void setTotalLocationsCount(int totalLocationsCount) {
		this.totalLocationsCount = totalLocationsCount;
	}

	public int getTotalLocationsCount() {
		return totalLocationsCount;
	}
}