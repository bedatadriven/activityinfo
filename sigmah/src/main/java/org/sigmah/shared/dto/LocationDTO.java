package org.sigmah.shared.dto;

import java.util.Map;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class LocationDTO extends BaseModelData implements DTO {

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
