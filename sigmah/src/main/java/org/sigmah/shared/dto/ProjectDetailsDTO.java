/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import org.sigmah.shared.dto.layout.LayoutDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.user.client.ui.Widget;

public class ProjectDetailsDTO extends BaseModelData implements EntityDTO {
    
	private static final long serialVersionUID = 3304868140991425311L;

	@Override
	public String getEntityName() {
		return "Project details";
	}
	
	@Override
    public int getId() {
        return (Integer)get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Reference to the Layout
    public LayoutDTO getLayoutDTO() {
        return get("layoutDTO");
    }

    public void setLayoutDTO(LayoutDTO layoutDTO) {
        set("layoutDTO", layoutDTO);
    }

    // Reference to the Project Model
    public ProjectModelDTO getProjectModelDTO() {
        return get("projectModelDTO");
    }

    public void setProjectModelDTO(ProjectModelDTO projectModelDTO) {
        set("projectModelDTO", projectModelDTO);
    }
    
	public Widget getWidget() {
		return getLayoutDTO().getWidget();
	}
    
}
