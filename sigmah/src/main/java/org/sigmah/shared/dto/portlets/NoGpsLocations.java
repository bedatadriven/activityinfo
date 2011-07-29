package org.sigmah.shared.dto.portlets;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.domain.Location;

public class NoGpsLocations implements PortletDTO {
	private List<Location> locations = new ArrayList<Location>();
	
	@Override
	public String getName() {
		return "Locations without gps coordinates";
	}

	@Override
	public String getDescription() {
		return "Lists all the locations which do not have GPS coordinates associated with them";
	}

	public List<Location> getLocations() {
		return locations;
	}

}
