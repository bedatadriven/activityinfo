package org.sigmah.shared.dto.portlets;


/*
 * A portlet that shows a list of locations which do not have an associated GPS coordinate
 */
public class NoGpsLocationsDTO implements PortletDTO {
	
	public NoGpsLocationsDTO() {
	}

	@Override
	public String getName() {
		return "Locations without gps coordinates";
	}

	@Override
	public String getDescription() {
		return "Lists all the locations which do not have GPS coordinates associated with them";
	}

}
