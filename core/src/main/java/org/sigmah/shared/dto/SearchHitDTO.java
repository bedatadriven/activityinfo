package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

/*
 * Encapsulates the DTO instance to have the ability for a rich user experience
 * in the UI for the search results. 
 */
public final class SearchHitDTO extends BaseModelData implements DTO {
	
	public SearchHitDTO() {
		set("name", "woei!");
	}	
	
	public SearchHitDTO(EntityDTO dto, int position) {
		this();
		
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
	public EntityDTO getDto() {
		return (EntityDTO) get("dto");
	}
	public void setDto(EntityDTO dto) {
		set("dto", dto);
		set("type", dto.getEntityName());
	}
	
	public BaseModelData getDtoAsBaseModel() {
		return getDto() == null ? null : (BaseModelData)getDto();
	}

	public void setType(String type) {
		set("type", type);
	}

	public String getType() {
		return getDto().getEntityName();
	}
}
