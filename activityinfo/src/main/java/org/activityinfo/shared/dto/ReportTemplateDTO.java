package org.activityinfo.shared.dto;

import java.util.ArrayList;
import java.util.List;


import com.extjs.gxt.ui.client.data.BaseModelData;

public class ReportTemplateDTO extends BaseModelData implements DTO {

	private List<ReportParameterDTO> parameters = new ArrayList<ReportParameterDTO>();
	
	public ReportTemplateDTO() {
		
	}

    public int getId() {
		return (Integer)get("id");
	}
	
	public void setId(int id) {
		set("id", id);
	}
	
	public void setTitle(String title) {
		set("title", title);
	}
	
	public String getTitle() {
		return get("title");
	}
	
	public void setDescription(String description) {
		set("description", description);
	}
	
	public String getDescription() {
		return get("description");
	}
	
	public Integer getDatabaseId() {
		return get("databaseId");
	}
	
	public void setDatabaseId(Integer id) {
		set("databaseId", id);
	}

    public String getDatabaseName() {
        return get("databaseName");
    }

    public void setDatabaseName(String name) {
        set("databaseName", name);
    }

	public boolean isEditAllowed() {
		return (Boolean)get("editAllowed");
	}
	
	public void setEditAllowed(boolean allowed) {
		set("editAllowed", allowed);
	}
	
	public String getOwnerName() {
		return get("ownerName");
	}
	
	public void setOwnerName(String name) {
		set("ownerName", name);
	}
	
	public List<ReportParameterDTO> getParameters() {
		return parameters;
	}

	public void setParameters(List<ReportParameterDTO> parameters) {
		this.parameters = parameters;
	}

	public void addParameter(ReportParameterDTO param) {
		this.parameters.add(param);
	}

	public void setAmOwner(boolean amOwner) {
		set("amOwner", amOwner);
	}
	
	public boolean getAmOwner() {
		return (Boolean)get("amOwner");
	}


    public int getSubscriptionFrequency() {
        return (Integer)get("subscriptionFrequency");
    }

    public void setSubscriptionFrequency(int frequency) {
        set("subscriptionFrequency", frequency);
    }

    public int getSubscriptionDay() {
        return (Integer)get("subscriptionDay");
    }

    public void setSubscriptionDay(int day) {
        set("subscriptionDay", day);
    }
    
}
