package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;


public class PartnerModel extends BaseModelData implements DTO {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 7436360195956315814L;
	
	public static final int MODEL_TYPE = 10;
		
	public PartnerModel()
	{
		
	}
	
	public PartnerModel(int id, String name) {
		setId(id);
		setName(name);
		
	}
	
	public void setId(int id) {
		set("id", id);
	}
	
	public int getId() {
		return (Integer)get("id");
	}
	
	public String getName()
	{
		return get("name");
	}
	
	public void setName(String value) {
		set("name", value);
	}
	
	public void setFullName(String value) {
		set("fullName", value);
	}

	public String getFullName() {
		return get("fullName");
	}

	public int getModelTypeId() {
		return MODEL_TYPE;
	}
	
	public boolean isOperational() {
		return true;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null)
			return false;
		if(other == this)
			return true;
		if(!(other instanceof PartnerModel))
			return false;
		
		PartnerModel that = (PartnerModel)other;
		
		return that.getId() == this.getId();
	}
	
	@Override
	public int hashCode() {
		if(get("id") == null)
			return 0;
		else
			return getId();
	}
	
}
