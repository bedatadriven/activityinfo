package org.activityinfo.client.page.entry.editor;


import org.activityinfo.shared.dto.AttributeGroupModel;
import org.activityinfo.shared.dto.AttributeModel;
import org.activityinfo.shared.dto.SiteModel;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;

public class AttributeCheckBoxGroup extends CheckBoxGroup {

	public AttributeCheckBoxGroup(AttributeGroupModel group) {
		
		this.setFieldLabel(group.getName());
		this.setOrientation(Orientation.VERTICAL);
		
		for(AttributeModel attrib : group.getAttributes() ) {
			
			CheckBox box = new CheckBox();
			box.setBoxLabel(attrib.getName());
			box.setName(attrib.getPropertyName());
			
			this.add(box);
		}
	}

}
