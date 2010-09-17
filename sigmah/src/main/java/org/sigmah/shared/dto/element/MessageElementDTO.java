/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import org.sigmah.shared.command.result.ValueResult;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Html;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * 
 */
public class MessageElementDTO extends FlexibleElementDTO {

    private static final long serialVersionUID = 8520711106031085130L;

    @Override
    public String getEntityName() {
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "element.MessageElement";
    }

    @Override
    public Component getComponent(ValueResult valueResult) {
        // The label for a message can be considered as HTML code.
        final Html message = new Html(getLabel());
        return message;
    }

    @Override
    public boolean isCorrectRequiredValue(ValueResult result) {
        // A message element cannot be a required element.
        return false;
    }

}
