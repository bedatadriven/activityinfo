/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import java.util.List;

import org.sigmah.shared.dto.value.ValueDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ProjectDTO extends BaseModelData implements EntityDTO {
    
	private static final long serialVersionUID = -8604264278832531036L;

	@Override
	public String getEntityName() {
		return "Project";
	}
	
	// Project id
	@Override
    public int getId() {
        return (Integer)get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Project name
    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    // Reference to the Project Model
    public ProjectModelDTO getProjectModelDTO() {
        return get("projectModelDTO");
    }

    public void setProjectModelDTO(ProjectModelDTO projectModelDTO) {
        set("projectModelDTO", projectModelDTO);
    }
    
    // Owner project name
    public String getOwnerName() {
        return get("ownerName");
    }

    public void setOwnerName(String ownerName) {
        set("ownerName", ownerName);
    }
    
    // Reference to the project phases list
    public List<PhaseDTO> getPhasesDTO() {
    	return get("phasesDTO");
    }
    
    public void setPhasesDTO(List<PhaseDTO> phasesDTO) {
    	set("phasesDTO", phasesDTO);
    }
    
    // Reference to the project values list
    public List<ValueDTO> getValuesDTO() {
    	return get("valuesDTO");
    }
    
    public void setValuesDTO(List<ValueDTO> valuesDTO) {
    	set("valuesDTO", valuesDTO);
    }
    
    // Reference to the current phase
    public PhaseDTO getCurrentPhaseDTO() {
    	return get("currentPhaseDTO");
    }
    
    public void setCurrentPhaseDTO(PhaseDTO currentPhaseDTO) {
    	set("currentPhaseDTO", currentPhaseDTO);
    }
    
}
