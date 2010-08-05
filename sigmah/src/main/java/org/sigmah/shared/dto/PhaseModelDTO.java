/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import java.util.List;

import org.sigmah.shared.dto.layout.LayoutDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.user.client.ui.Widget;

public class PhaseModelDTO extends BaseModelData implements EntityDTO {
    
	private static final long serialVersionUID = 8520711106031085130L;

	@Override
	public String getEntityName() {
		return "Phase model";
	}
	
	// Phase model id
	@Override
    public int getId() {
        return (Integer)get("id");
    }

    public void setId(int id) {
        set("id", id);
    }
    
    // Phase model name
    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    // Reference to parent project model DTO
    public ProjectModelDTO getParentProjectModelDTO() {
        return get("parentProjectModelDTO");
    }

    public void setParentProjectModelDTO(ProjectModelDTO parentProjectModelDTO) {
        set("parentProjectModelDTO", parentProjectModelDTO);
    }
    
    // Reference to layout
    public LayoutDTO getLayoutDTO() {
        return get("layoutDTO");
    }

    public void setLayoutDTO(LayoutDTO layoutDTO) {
        set("layoutDTO", layoutDTO);
    }
    
    // Reference to the phases successors
    public List<PhaseModelDTO> getSuccessorsDTO() {
        return get("successorsDTO");
    }

    public void setSuccessorsDTO(List<PhaseModelDTO> successorsDTO) {
        set("successorsDTO", successorsDTO);
    }
    
    // Display order
    public int getDisplayOrder() {
        return (Integer) get("displayOrder");
    }

    public void setDisplayOrder(int displayOrder) {
        set("displayOrder", displayOrder);
    }
    
	public Widget getWidget() {
		return getLayoutDTO().getWidget();
	}
    
}
