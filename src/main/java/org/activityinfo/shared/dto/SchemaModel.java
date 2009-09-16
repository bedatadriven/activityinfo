package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModel;

public class SchemaModel extends BaseModel {

	public SchemaModel() {
		
	}
	
	public int getId() {
		return (Integer)get("id");
	}
	
	public void setId(int id) {
		set("id", id);
	}
	
	
}
