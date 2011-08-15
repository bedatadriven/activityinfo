package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

/*
 * Encapsulates the DTO instance to have the ability for a rich user experience
 * in the UI for the search results. 
 */
public class SearchHitDTO extends BaseModelData implements DTO {
	private String type;
	
	public SearchHitDTO() {
		set("name", "woei!");
	}	
	
	public SearchHitDTO(DTO dto, int position) {
		setDto(dto);
		setPosition(position);
	}
	
	public float getBoost() {
		return (Float)get("boost");
	}
	public void setBoost(float boost) {
		set("boost", boost);
	}
	public int getPosition() {
		return (Integer) get("position");
	}
	public void setPosition(int position) {
		set("position",  position);
	}
	public DTO getDto() {
		return (DTO) get("dto");
	}
	public void setDto(DTO dto) {
		set("dto", dto);
	}
	
	public BaseModelData getDtoAsBaseModel() {
		return getDto() == null ? null : (BaseModelData)getDto();
	}

	public void setType(String type) {
		set("type", type);
	}

	public String getType() {
		return (String)get("type");
		
	}
}
