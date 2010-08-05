/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import org.sigmah.shared.command.result.ValueResult;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.TextArea;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 *
 */
public class TextAreaElementDTO extends FlexibleElementDTO {
    
	private static final long serialVersionUID = 8520711106031085130L;

	@Override
	public String getEntityName() {
		return "Flexible Element > text area";
	}
	
	@Override
	public Component getComponent(ValueResult valueResult) {
		TextArea textarea = new TextArea();
		textarea.setFieldLabel(getLabel());
		textarea.setWidth("100%");
		handleRequiredElement(textarea);
		
		if (valueResult != null) {
			textarea.setValue((String) valueResult.getValueObject());
		}
		
		return textarea;
	}
    
}
