package org.activityinfo.client.page.entry.editor;


import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;

public class AttributeCheckBoxGroup extends CheckBoxGroup {

	public AttributeCheckBoxGroup(AttributeGroupDTO group) {
		
		this.setFieldLabel(group.getName());
		this.setOrientation(Orientation.VERTICAL);
		
		for(AttributeDTO attrib : group.getAttributes() ) {
			
			CheckBox box = new CheckBox();
			box.setBoxLabel(attrib.getName());
			box.setName(attrib.getPropertyName());
			
			this.add(box);
		}
	}

}
