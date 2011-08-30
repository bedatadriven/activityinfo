package org.sigmah.shared.command;

import java.util.Map;

import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.RpcMap;

public class CreateSite implements MutatingCommand<CreateResult> {

	private RpcMap properties;

	public CreateSite() {
		properties = new RpcMap();
	}
	
	public CreateSite(SiteDTO site) {
        properties = site.toChangeMap();
	}
	
	
	public CreateSite(Map<String, Object> properties) {
		this.properties = new RpcMap();
		this.properties.putAll(properties);
	}

	public int getActivityId() {
		return (Integer)properties.get("activityId");
	}
	
	public int getPartnerId() {
		return (Integer)properties.get("partnerId");
	}

	public RpcMap getProperties() {
		return properties;
	}

	public void setProperties(RpcMap properties) {
		this.properties = properties;
	}	
	
}
