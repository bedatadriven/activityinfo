/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.dto.element.handler.RequiredValueEvent;
import org.sigmah.shared.dto.element.handler.ValueEvent;

import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
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
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "element.QuestionElement";
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
        emptyChoice.setId(-1);
        emptyChoice.setLabel(I18N.CONSTANTS.flexibleElementQuestionEmptyChoice());
        store.add(emptyChoice);

        store.add(getChoicesDTO());
        comboBox.setStore(store);
        comboBox.setFieldLabel(getLabel());
        comboBox.setDisplayField("label");
        comboBox.setValueField("id");
        comboBox.setLabelSeparator("");
        comboBox.setTriggerAction(TriggerAction.ALL);
        comboBox.setEditable(false);

        comboBox.setAllowBlank(true);

        if (valueResult != null && valueResult.getValueObject() != null) {
            String idChoice = (String) valueResult.getValueObject();
            for (QuestionChoiceElementDTO choiceDTO : getChoicesDTO()) {
                if (idChoice.equals(String.valueOf(choiceDTO.getId()))) {
                    comboBox.setValue(choiceDTO);
                    break;
                }
            }
        }

        // Listens to the selection changes.
        comboBox.addSelectionChangedListener(new ComboBoxSelectionListener());

        return comboBox;
    }

    @Override
    public boolean isCorrectRequiredValue(ValueResult result) {

        if (result == null || result.getValueObject() == null) {
            return false;
        }

        try {
            final String value = (String) result.getValueObject();
            return value != null && !"".equals(value);
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Basic selection changes listener implementation to fire value changes
     * events of the current flexible element.
     * 
     * @author tmi
     * 
     */
    private class ComboBoxSelectionListener extends SelectionChangedListener<QuestionChoiceElementDTO> {

        @Override
        public void selectionChanged(SelectionChangedEvent<QuestionChoiceElementDTO> se) {

            // Gets the selected choice.
            final QuestionChoiceElementDTO choice = se.getSelectedItem();

            handlerManager.fireEvent(new ValueEvent(QuestionElementDTO.this, choice, ValueEvent.ChangeType.EDIT));

            // Required element ?
            if (getValidates()) {

                // Checks if the choice isn't the default empty choice.
                boolean isValueOn = choice != null && choice.getId() != -1;
                handlerManager.fireEvent(new RequiredValueEvent(isValueOn));
            }
        }

    }
}
