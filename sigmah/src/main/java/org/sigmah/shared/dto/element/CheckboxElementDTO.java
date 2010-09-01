/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.dto.element.handler.RequiredValueEvent;
import org.sigmah.shared.dto.element.handler.ValueEvent;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
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
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "element.CheckboxElement";
    }

    @Override
    public Component getComponent(ValueResult valueResult) {
        final CheckBox checkbox = new CheckBox();
        checkbox.setBoxLabel(getLabel());
        checkbox.setHideLabel(true);

        if (valueResult != null && valueResult.getValueObject() != null) {
            String value = (String) valueResult.getValueObject();
            checkbox.setValue(value.equalsIgnoreCase("true"));
        }

        checkbox.addListener(Events.OnClick, new CheckBoxListener());

        return checkbox;
    }

    @Override
    public boolean isCorrectRequiredValue(ValueResult result) {

        if (result == null || result.getValueObject() == null) {
            return false;
        }

        try {
            final String value = (String) result.getValueObject();
            return value.equalsIgnoreCase("true");
        } catch (ClassCastException e) {
            return false;
        }
    }

    private class CheckBoxListener implements Listener<BaseEvent> {

        @Override
        public void handleEvent(BaseEvent be) {
            CheckBox checkbox = (CheckBox) be.getSource();
            boolean value = checkbox.getValue();

            handlerManager.fireEvent(new ValueEvent(CheckboxElementDTO.this, value, ValueEvent.ChangeType.EDIT));

            // Required element ?
            if (getValidates()) {
                handlerManager.fireEvent(new RequiredValueEvent(value));
            }
        }

    }

}
