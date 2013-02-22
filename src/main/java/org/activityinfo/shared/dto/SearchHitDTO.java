package org.activityinfo.shared.dto;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
