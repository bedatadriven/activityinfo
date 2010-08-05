/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.value;

import org.sigmah.shared.dto.EntityDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.element.FlexibleElementDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 *
 */
public class ValueDTO extends BaseModelData implements EntityDTO {
    
	private static final long serialVersionUID = 8520711106031085130L;
	
	@Override
	public String getEntityName() {
		return "Value";
	}
	
	// Value id
	@Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }
    
    // Reference to the parent project
    public ProjectDTO getParentProjectDTO() {
        return get("parentProjectDTO");
    }

    public void setParentProjectDTO(ProjectDTO parentProjectDTO) {
        set("parentProjectDTO", parentProjectDTO);
    }
    
    // Reference to the associated flexible element
    public FlexibleElementDTO getFlexibleElementDTO() {
        return get("flexibleElementDTO");
    }

    public void setFlexibleElementDTO(FlexibleElementDTO flexibleElementDTO) {
        set("flexibleElementDTO", flexibleElementDTO);
    }
    
    // Value's inner value
    public String getValue() {
        return get("value");
    }

    public void setValue(String value) {
        set("value", value);
    }
    
}
