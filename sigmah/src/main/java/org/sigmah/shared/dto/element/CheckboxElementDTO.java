/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import org.sigmah.shared.command.result.ValueResult;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.CheckBox;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 *
 */
public class CheckboxElementDTO extends FlexibleElementDTO {
    
	private static final long serialVersionUID = 8520711106031085130L;

	@Override
	public String getEntityName() {
		return "Flexible Element > checkbox";
	}
	
	@Override
	public Component getComponent(ValueResult valueResult) {
		CheckBox checkbox = new CheckBox();
		checkbox.setBoxLabel(getLabel());
		checkbox.setHideLabel(true);
		handleRequiredElement(checkbox);
		
		if (valueResult != null && valueResult.getValueObject() != null) {
			String value = (String) valueResult.getValueObject();
			checkbox.setValue(value.equalsIgnoreCase("true"));
		}
		
		return checkbox;
	}

    
}
