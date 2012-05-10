package org.activityinfo.shared.dto;

import java.util.List;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class LocationDTO extends BaseModelData implements EntityDTO, HasAdminEntityValues {
	
	public LocationDTO() {
		super();
	}
	
	public LocationDTO(LocationDTO location) {
		super(location.getProperties());
	}

	public static LocationDTO fromSqlRow(SqlResultSetRow row) {
		String name = row.getString("Name");
		String axe = row.isNull("Axe") ? null : row.getString("Axe");
		Double longitude = row.isNull("X") ? null : row.getDouble("X");
		Double latitude = row.isNull("Y") ? null : row.getDouble("Y");
		int id = row.getInt("LocationId");
		
		return new LocationDTO()
			.setId(id)
			.setName(name)
			.setAxe(axe)
			.setLongitude(longitude)
			.setLatitude(latitude);
	}

	@Override
	public String getName() {
		return (String)get("name");
	}

	public LocationDTO setName(String name) {
		set("name", name);
		return this;
	}

	public String getAxe() {
		return (String)get("axe");
	}

	public LocationDTO setAxe(String axe) {
		set("axe", axe);
		return this;
	}

	public Double getLatitude() {
		return (Double)get("latitude");
	}

	public LocationDTO setLatitude(Double latitude) {
		set("latitude", latitude);
		return this;
	}

	public Double getLongitude() {
		return (Double)get("longitude");
	}

	public LocationDTO setLongitude(Double longitude) {
		set("longitude", longitude);
		return this;
	}

	public LocationDTO setId(int id) {
		set("id", id);
		return this;
	}

	@Override
	public int getId() {
		return (Integer)get("id");
	}
	
	public int getLocationTypeId() {
		return (Integer)get("locationTypeId");
	}
	
	public LocationDTO setLocationTypeId(int locationTypeId) {
		set("locationTypeId", locationTypeId);
		return this;
	}
	
	public LocationDTO setAdminEntity(int levelId, AdminEntityDTO value) {
		set(AdminLevelDTO.getPropertyName(levelId), value);
		return this;
	}

	@Override
	public AdminEntityDTO getAdminEntity(int levelId) {
		return (AdminEntityDTO)get(AdminLevelDTO.getPropertyName(levelId));
	}
	
	public List<AdminEntityDTO> getAdminEntities() {
		List<AdminEntityDTO> list = Lists.newArrayList();
		for(String property : getPropertyNames()) {
			if(property.startsWith(AdminLevelDTO.PROPERTY_PREFIX)) {
				AdminEntityDTO entity = (AdminEntityDTO)get(property);
				if(entity != null) {
					list.add(entity);
				}
			}
		}
		return list;
	}
	
	public String getMarker() {
		return get("marker");
	}
	
	public void setMarker(String marker) {
		set("marker", marker);
	}
	
	@Override
	public String getEntityName() {
		return "Location";
	}
	/** True when latitude() and longitude() are non-null */
	public boolean hasCoordinates() {
		return getLatitude() != null && getLongitude() != null;
	}
	/** True when this Location has a non-empty Axe */
	public boolean hasAxe() {
		return !Strings.isNullOrEmpty(getAxe());
	}
}
