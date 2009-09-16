package org.activityinfo.client.page.entry.editor;

import org.activityinfo.client.Application;
import org.activityinfo.client.page.entry.editor.AttributeCheckBoxGroup;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.AttributeGroupModel;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class AttributeFieldSet extends AbstractFieldSet {

    public AttributeFieldSet(ActivityModel activity) {
        super(Application.CONSTANTS.attributes(), 100, 300);

		for(AttributeGroupModel attributeGroup : activity.getAttributeGroups()) {

			AttributeCheckBoxGroup boxGroup = new AttributeCheckBoxGroup(attributeGroup);
			boxGroup.setStyleAttribute("marginBottom", "10px");

            add(boxGroup);
        }
    }

}
