/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.dto.EntityDTO;
import org.sigmah.shared.dto.ProjectModelDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 *
 */
public abstract class FlexibleElementDTO extends BaseModelData implements EntityDTO {
    
	private static final long serialVersionUID = 8520711106031085130L;
	
	/**
	 * Gets the widget of a flexible element with its value.
	 * 
	 * @param valueResult value of the flexible element, or {@code null} to display 
	 * the element without its value.
	 * 
	 * @return the widget corresponding to the flexible element.
	 */
	public abstract Component getComponent(ValueResult valueResult);
	
	// Flexible element id
	@Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }
    
    // Flexible element label
    public String getLabel() {
        return get("label");
    }

    public void setLabel(String label) {
        set("label", label);
    }
    
    // Flexible element validates
    public boolean getValidates() {
        return (Boolean) get("validates");
    }

    public void setValidates(boolean validates) {
        set("validates", validates);
    }
    
	// Reference to the parent project model
    public ProjectModelDTO getParentProjectModelDTO() {
        return get("parentProjectModelDTO");
    }

    public void setParentProjectModelDTO(ProjectModelDTO parentProjectModelDTO) {
        set("parentProjectModelDTO", parentProjectModelDTO);
    }
    
    /**
     * Handles the required flexible elements by adding a specific css style 
     * to the widget.
     * This method is called by the sub classes.
     * 
     * @param widget 
     * 			widget to update
     */
    protected void handleRequiredElement(Widget widget) {
    	widget.addStyleName("sigmah-element");
    	if (getValidates()) {
    		widget.addStyleName("required");
    	}
    }
    
    public boolean getFilledIn() {
    	return (Boolean) get("filledIn");
    }
    
    public void setFilledIn(boolean filledIn) {
    	set("filledIn", filledIn);
    }
    
}
