package org.activityinfo.client.page.entry.editor;

import org.activityinfo.client.Application;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;

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
