/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.form;

import org.sigmah.client.page.entry.form.field.AttributeCheckBoxGroup;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;

public class AttributeSection extends FormSection {

    public AttributeSection(ActivityDTO activity) {
    	
		for(AttributeGroupDTO attributeGroup : activity.getAttributeGroups()) {

			AttributeCheckBoxGroup boxGroup = new AttributeCheckBoxGroup(attributeGroup);
			boxGroup.setStyleAttribute("marginBottom", "10px");
            boxGroup.setStyleAttribute("width", "100%");  // if the width is specified in px, IE6 flips out 

            add(boxGroup);
        }
    }

}
