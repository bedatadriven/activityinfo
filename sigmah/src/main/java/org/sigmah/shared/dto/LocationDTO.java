package org.sigmah.shared.dto;

import java.util.Map;

import org.sigmah.shared.domain.LocationType;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class LocationDTO extends BaseModelData implements DTO {
	private int id;
    private LocationType locationType;
	private String name;
	
	public LocationDTO(int id, LocationType locationType, String name) {
		super();
		this.id = id;
		this.locationType = locationType;
		this.name = name;
	}

	public LocationDTO() {
	}

	public LocationDTO(Map<String, Object> properties) {
		super(properties);
	}

	public int getId() {
		return (Integer)get("id");
	}

	public void setId(int id) {
		set("id", id);
	}

	public LocationType getLocationType() {
		return (LocationType)get("locationType");
	}

	public void setLocationType(LocationType locationType) {
		set("locationType", locationType);
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
}
