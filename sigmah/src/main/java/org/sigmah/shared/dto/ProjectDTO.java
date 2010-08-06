/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import java.util.Date;
import java.util.List;

import org.sigmah.shared.dto.value.ValueDTO;


public final class ProjectDTO extends BaseTreeModel implements EntityDTO {

    public ProjectDTO() {
        // TODO: Remove this
        setTopic("Nutrition");
        setFavorite(new Date().getTime()%10 < 5);
    }
    
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
    
    public boolean isFavorite() {
        return (Boolean) get("favorite");
    }
    public void setFavorite(boolean favorite) {
        set("favorite", favorite);
    }
    
    public String getTopic() {
        return get("topic");
    }
    public void setTopic(String topic) {
        set("topic", topic);
    }
}
