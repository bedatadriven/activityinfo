package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ProjectDTO extends BaseModelData implements DTO {
	private String description;
	public ProjectDTO() {
		super();
	}

	public ProjectDTO(int id, String name) {
		super();
		
		setId(id);
		setName(name);
	} 

	public int getId() {
		return (Integer)get("id");
	}
	
	public void setId(int id) {
		set("id", id); 
	}
	
	public String getName() {
		return (String)get("name");
	}
	
	public void setName(String name) {
		set("name", name);
	}
	
	public void setDescription(String description) {
		set("description", description);
	}

	public String getDescription() {
		return (String)get("description");
	}
}