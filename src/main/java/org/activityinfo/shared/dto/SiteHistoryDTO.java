package org.activityinfo.shared.dto;

import java.util.Date;
import java.util.Map;

import org.activityinfo.shared.util.JsonUtil;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gson.JsonObject;

public final class SiteHistoryDTO extends BaseModelData implements EntityDTO {
	private static final long serialVersionUID = 4951231064643225215L;

	public static final String ENTITY_NAME = "SiteHistory";

	public SiteHistoryDTO() {
		set("name", " ");
	}

    public SiteHistoryDTO(int id) {
    	this();
        setId(id);
    }

    public int getId() {
    	return (Integer)get("id");
    }
    public void setId(int id) {
		set("id", id);
	}
    
	public String getUserName() {
		return get("userName");
	}
	public void setUserName(String userName) {
		set("userName", userName);
	}

	public String getUserEmail() {
		return get("userEmail");
	}
	public void setUserEmail(String userEmail) {
		set("userEmail", userEmail);
	}
	
	public boolean isInitial() {
		return get("initial", false);
	}
	public void setInitial(boolean initial) {
		set("initial", initial);
	}
	
	public String getJson() {
		return get("json");
	}
	public JsonObject getJsonObject() {
		return JsonUtil.parse(getJson());
	}
	public Map<String, Object> getJsonMap() {
		return JsonUtil.decodeMap(getJsonObject());
	}
	public void setJson(String json) {
		set("json", json);
	}
	
	public long getTimeCreated() {
		return (Long)get("timeCreated");
	}
	public Date getDateCreated() {
		return new Date(getTimeCreated());
	}
	public void setTimeCreated(long timeCreated) {
		set("timeCreated", timeCreated);
	}
	public void setTimeCreated(double timeCreated) {
		setTimeCreated((long)timeCreated);
	}
	
	public SiteHistoryDTO copy() {
		SiteHistoryDTO copy = new SiteHistoryDTO();
		copy.setProperties(this.getProperties());
		return copy;
	}

	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}

	@Override
	public String getName() {
		// TODO Leave unimplemented for now 
		return null;
	}
}
