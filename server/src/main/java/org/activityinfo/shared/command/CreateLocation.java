package org.activityinfo.shared.command;

import java.util.Map;

import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.LocationDTO;

import com.extjs.gxt.ui.client.data.RpcMap;

public class CreateLocation implements MutatingCommand<VoidResult>{
	
	private RpcMap properties;
	
	public CreateLocation() {
		properties = new RpcMap();
	}
	
	public CreateLocation(LocationDTO location) {
		properties = new RpcMap();
		properties.put("id", location.getId());
		properties.put("locationTypeId", location.getLocationTypeId());
		properties.put("name", location.getName());
		properties.put("axe", location.getAxe());
		properties.put("latitude", location.getLatitude());
		properties.put("longitude", location.getLongitude());
		
		for(AdminEntityDTO entity : location.getAdminEntities()) {
			properties.put(AdminLevelDTO.getPropertyName(entity.getLevelId()), entity.getId());
		}	
	}
	
	public CreateLocation(Map<String, Object> properties) {
		this.properties = new RpcMap();
		this.properties.putAll(properties);
	}

	public RpcMap getProperties() {
		return properties;
	}

	public void setProperties(RpcMap properties) {
		this.properties = properties;
	}
	
}
