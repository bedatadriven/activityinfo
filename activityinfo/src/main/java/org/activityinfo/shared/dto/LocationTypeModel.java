package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

import java.util.Map;


public class LocationTypeModel extends BaseModelData implements DTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2696877826228221255L;
	
	public static final int MODEL_TYPE = 992;
	
	public LocationTypeModel()
	{
	
	}

    public LocationTypeModel(Map<String, Object> properties) {
        super(properties);
    }

    public LocationTypeModel(int id, String name) {
        setId(id);
        setName(name);
    }

    public void setId(int id) {
		set("id", id);
	}
	
	public int getId() {
		return (Integer)get("id");
	}
	
	public void setName(String value) {
		set("name", value);
	}
	
	public String getName() { 
		return get("name");
	}
	
	public Integer getBoundAdminLevelId() {
		return get("boundAdminLevelId");
	}
	
	public void setBoundAdminLevelId(Integer id) {
		set("boundAdminLevelId", id);
	}

	public boolean isAdminLevel() {
		return getBoundAdminLevelId() != null;
	}

}
