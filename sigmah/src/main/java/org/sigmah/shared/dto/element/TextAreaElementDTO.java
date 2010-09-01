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
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "element.TextAreaElement";
    }

    @Override
    public Component getComponent(ValueResult valueResult) {
        TextArea textarea = new TextArea();
        textarea.setFieldLabel(getLabel());
        textarea.setWidth("100%");

        if (valueResult != null) {
            textarea.setValue((String) valueResult.getValueObject());
        }

        textarea.addListener(Events.OnKeyUp, new TextAreaListener());

        return textarea;
    }

    @Override
    public boolean isCorrectRequiredValue(ValueResult result) {

        if (result == null || result.getValueObject() == null) {
            return false;
        }

        try {
            final String value = (String) result.getValueObject();
            return value != null && !"".equals(value.trim());
        } catch (ClassCastException e) {
            return false;
        }
    }

    private class TextAreaListener implements Listener<BaseEvent> {

        @Override
        public void handleEvent(BaseEvent be) {
            TextArea textArea = (TextArea) be.getSource();
            String value = textArea.getValue();

            if(value == null)
                value = "";

            boolean isValueOn = !value.trim().equals("");

            handlerManager.fireEvent(new ValueEvent(TextAreaElementDTO.this, value, ValueEvent.ChangeType.EDIT));

            // Required element ?
            if (getValidates()) {
                handlerManager.fireEvent(new RequiredValueEvent(isValueOn));
            }
        }

    }

}
