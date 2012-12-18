package org.activityinfo.shared.command;

import java.util.Map;
import java.util.Map.Entry;

import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.extjs.gxt.ui.client.data.RpcMap;

public class CreateSite implements MutatingCommand<CreateResult>, SiteCommand {

	private RpcMap properties;
	
	// ensure this class is cleared for deserialization
	private LocalDate date_;

	public CreateSite() {
		properties = new RpcMap();
	}
	
	public CreateSite(SiteDTO site) {
		assert site.getId() != 0;
		assert site.getActivityId() != 0;
		assert site.getLocationId() != 0;
		assert site.getPartner() != null;
						
        properties = new RpcMap();
	    for(Entry<String, Object> property : site.getProperties().entrySet()) {
	    	if(property.getKey().equals("partner")) {
	    		properties.put("partnerId", site.getPartner().getId());
	    	} else if(property.getKey().equals("project")) {
	    		if(site.getProject() != null) {
	    			properties.put("projectId", site.getProject().getId());
	    		}
	    	} else if( !property.getKey().startsWith(AdminLevelDTO.PROPERTY_PREFIX) ){
	    		properties.put(property.getKey(), property.getValue());
	    	}
	    }
	}
	
	
	public CreateSite(Map<String, Object> properties) {
		this.properties = new RpcMap();
		this.properties.putAll(properties);
	}

	public int getSiteId() {
		return (Integer)properties.get("id");
	}
	
	public int getActivityId() {
		return (Integer)properties.get("activityId");
	}
	
	public int getPartnerId() {
		return (Integer)properties.get("partnerId");
	}
	
	public int getLocationId() {
		return (Integer)properties.get("locationId");
	}

	public Integer getReportingPeriodId() {
		return (Integer)properties.get("reportingPeriodId");
	}
	
	public RpcMap getProperties() {
		return properties;
	}

	public void setProperties(RpcMap properties) {
		this.properties = properties;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof CreateSite)) {
			return false;
		}
		return this.properties.equals(((CreateSite)obj).properties);
	}

	@Override
	public int hashCode() {
		return properties.hashCode();
	}

	@Override
	public String toString() {
		return "CreateSite{" + properties.toString() + "}";
	}	
}
