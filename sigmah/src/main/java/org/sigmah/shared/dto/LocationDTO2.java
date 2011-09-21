package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class LocationDTO2 extends BaseModelData implements EntityDTO {
	public String getName() {
		return get("name");
	}

	public LocationDTO2 setName(String name) {
		set("name", name);
		return this;
	}

	public String getAxe() {
		return get("axe");
	}

	public LocationDTO2 setAxe(String axe) {
		set("axe", axe);
		return this;
	}

	public Double getLatitude() {
		return get("latitude");
	}

	public LocationDTO2 setLatitude(Double latitude) {
		set("latitude", latitude);
		return this;
	}

	public Double getLongitude() {
		return get("longitude");
	}

	public LocationDTO2 setLongitude(Double longitude) {
		set("longitude", longitude);
		return this;
	}

	public LocationDTO2 setId(int id) {
		set("id", id);
		return this;
	}

	@Override
	public int getId() {
		return get("id");
	}
	
	public int getLocationTypeId() {
		return get("locationTypeId");
	}
	
	public LocationDTO2 setLocationTypeId(int locationTypeId) {
		set("locationTypeId", locationTypeId);
		return this;
	}
	
	public LocationDTO2 setAdminEntity(int levelId, AdminEntityDTO value) {
		set(AdminLevelDTO.getPropertyName(levelId), value);
		return this;
	}

	public AdminEntityDTO getAdminEntity(int levelId) {
		return get(AdminLevelDTO.getPropertyName(levelId));
	}
	
	@Override
	public String getEntityName() {
		return "Location";
	}

}
