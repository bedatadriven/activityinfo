/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import org.sigmah.client.Application;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class AttributeFieldSet extends AbstractFieldSet {

    public AttributeFieldSet(ActivityDTO activity) {
        super(Application.CONSTANTS.attributes(), 100, 300);

		for(AttributeGroupDTO attributeGroup : activity.getAttributeGroups()) {

			AttributeCheckBoxGroup boxGroup = new AttributeCheckBoxGroup(attributeGroup);
			boxGroup.setStyleAttribute("marginBottom", "10px");
            boxGroup.setStyleAttribute("width", "100%");  // if the width is specified in px, IE6 flips out 

            add(boxGroup);
        }
    }

}
