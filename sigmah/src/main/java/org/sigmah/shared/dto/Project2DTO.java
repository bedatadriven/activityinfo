package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class Project2DTO extends BaseModelData implements DTO {
	private String description;
	public Project2DTO() {
		super();
	}

//	public Project2DTO(int id, String name, int siteId) {
	public Project2DTO(int id, String name) {
		super();
		
		setId(id);
		setName(name);
//		setSiteId(siteId); 
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
	
//	public int getSiteId() {
//		return (Integer)get("siteId");
//	}
//	
//	public void setSiteId(int siteId) {
//		set("siteId", siteId);
//	}

	public void setDescription(String description) {
		set("description", description);
	}

	public String getDescription() {
		return (String)get("description");
	}
}