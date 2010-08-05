/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import java.util.List;

import org.sigmah.shared.command.result.ValueResult;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 *
 */
public class QuestionElementDTO extends FlexibleElementDTO {
    
	private static final long serialVersionUID = 8520711106031085130L;

	@Override
	public String getEntityName() {
		return "Flexible Element > question";
	}
	
	// Question choices list
	public List<QuestionChoiceElementDTO> getChoicesDTO() {
		return get("choicesDTO");
	}

	public void setChoicesDTO(List<QuestionChoiceElementDTO> choicesDTO) {
		set("choicesDTO", choicesDTO);
	}
	
	@Override
	public Component getComponent(ValueResult valueResult) {
		ComboBox<QuestionChoiceElementDTO> comboBox = new ComboBox<QuestionChoiceElementDTO>();
		ListStore<QuestionChoiceElementDTO> store = new ListStore<QuestionChoiceElementDTO>();
		
		// Empty choice
		QuestionChoiceElementDTO emptyChoice = new QuestionChoiceElementDTO();
		emptyChoice.setLabel("");
		store.add(emptyChoice);
		
		store.add(getChoicesDTO());
		comboBox.setStore(store);
		comboBox.setFieldLabel(getLabel());
		comboBox.setDisplayField("label");
		comboBox.setValueField("id");
		comboBox.setLabelSeparator("");
		comboBox.setTriggerAction(TriggerAction.ALL);
		comboBox.setEditable(false);
		
		handleRequiredElement(comboBox);
		
		if (valueResult != null && valueResult.getValueObject() != null) {
			String idChoice = (String) valueResult.getValueObject();
			for (QuestionChoiceElementDTO choiceDTO : getChoicesDTO()) {
				if (idChoice.equals(String.valueOf(choiceDTO.getId()))) {
					comboBox.setValue(choiceDTO);
					break;
				}
			}
		}
		
		return comboBox;
	}
    
}
