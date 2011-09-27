package org.sigmah.client.page.entry.editor;

import org.sigmah.shared.dto.LocationDTO2;

import com.extjs.gxt.ui.client.data.BaseModelData;


/** Reflective Location model for display in a GXT ListView
 * A seperate ViewModel is necessary to enable support of more properties, without
 * the need to send as data over the wire */
public class LocationViewModel extends BaseModelData {
	public LocationViewModel(LocationDTO2 location, String marker) {
		super();
		set("marker", marker);
		
		set("id", location.getId());
		set("name", location.getName()); 
		set("axe", location.getAxe());
		set("longitude", location.getLongitude());
		set("latitude", location.getLatitude());
		set("hasAxe", location.hasAxe());
		set("hasCoordinates", location.hasCoordinates());
	}
	public String getName() {
		return (String)get("name");
	}
	public void setName(String name) {
		set("name", name);
	}
	public String getAxe() {
		return (String)get("axe");
	}
	public void setAxe(String axe) {
		set("axe", axe);
	}
	public Double getLatitude() {
		return (Double)get("latitude");
	}
	public void setLatitude(double latitude) {
		set("latitude", latitude);
	}
	public Double getLongitude() {
		return (Double)get("longitude");
	}
	public void setLongitude(double longitude) {
		set("longitude", longitude);
	}
	public int getId() {
		return (Integer)get("id");
	}
	public void setId(int id) {
		set("id", id);
	}
	public String locationType() {
		return (String) get("locationType");
	}
	public void setLocationType(String locationType) {
		set("locationType", locationType);
	}
	public boolean hasCoordinates() {
		return (Boolean)get("hasCoordinates");
	}
	public void setHasCoordinates(boolean hasCoordinates) {
		set("hasCoordinates", hasCoordinates);
	}
	public boolean hasAxe() {
		return (Boolean) get("hasAxe");
	}
	public void setHasAxe(boolean hasAxe) {
		set("hasAxe", hasAxe);
	}
	public String marker() {
		return (String) get("marker");
	}
	public void setMarker(String marker) {
		set("marker", marker);
	}
}
