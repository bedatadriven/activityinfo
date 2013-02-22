

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


/**
 * One-to-one DTO of the {@link org.activityinfo.server.database.hibernate.entity.Partner} domain class.
 *
 * @author Alex Bertram
 */
public final class PartnerDTO extends BaseModelData implements DTO, ProvidesKey {

	public PartnerDTO() {
		
	}
	
	public PartnerDTO(int id, String name) {
		setId(id);
		setName(name);
	}
	
	public void setId(int id) {
		set("id", id);
	}
	
	public int getId() {
		return (Integer)get("id");
	}
	
	public String getName()	{
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

    public boolean isOperational() {
		return true;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) {
            return false;
        }
		if(other == this) {
            return true;
        }
		if(!(other instanceof PartnerDTO)) {
            return false;
        }
		
		PartnerDTO that = (PartnerDTO)other;
		
		return that.getId() == this.getId();
	}
	
	@Override
	public int hashCode() {
		if(get("id") == null) {
            return 0;
        } else {
            return getId();
        }
	}

	@Override
	public String getKey() {
		return "partner" + getId();
	}
	
}
