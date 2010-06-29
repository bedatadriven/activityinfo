/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * One-to-one DTO for the {@link org.sigmah.server.domain.AttributeGroup} domain object
 *
 * @author Alex Bertram
 */
public final class AttributeGroupDTO extends BaseModel implements EntityDTO {

	private List<AttributeDTO> attributes = new ArrayList<AttributeDTO>(0);
	
	public AttributeGroupDTO() {
	}

    /**
     * Creates a shallow clone
     * @param model
     */
    public AttributeGroupDTO(AttributeGroupDTO model) {
        super(model.getProperties());
        setAttributes(model.getAttributes());
    }
	
	public AttributeGroupDTO(int id) {
		this.setId(id);
	}

    public int getId() {
        return (Integer)get("id");
    }

    public void setId(int id) {
        set("id",  id);
    }

    public void setName(String name) {
		set("name", name);
	}
	
	public String getName() {
		return get("name");
	}

	public List<AttributeDTO> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(List<AttributeDTO> attributes) {
		this.attributes = attributes;		
	}

	public boolean isMultipleAllowed() {
		return (Boolean)get("multipleAllowed");
	}
	
	public void setMultipleAllowed(boolean allowed) {
		set("multipleAllowed", allowed);
	}

    public String getEntityName() {
        return "AttributeGroup";
    }
}
