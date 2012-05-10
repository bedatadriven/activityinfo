/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.entry.form.field;


import java.util.List;

import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.google.common.collect.Lists;

public class AttributeCheckBoxGroup extends CheckBoxGroup implements AttributeField {

	private List<CheckBox> checkBoxes;
	
	public AttributeCheckBoxGroup(AttributeGroupDTO group) {
		assert group != null;
	
		this.setFieldLabel(group.getName());
		this.setOrientation(Orientation.VERTICAL);
		
		checkBoxes = Lists.newArrayList();
		for(AttributeDTO attrib : group.getAttributes() ) {
			
			CheckBox box = new CheckBox();
			box.setBoxLabel(attrib.getName());
			box.setName(attrib.getPropertyName());
			
			this.add(box);
			checkBoxes.add(box);
		}
	}

	@Override
	public void updateForm(SiteDTO site) {
		for(CheckBox checkBox : checkBoxes) {
			checkBox.setValue((Boolean) site.get(checkBox.getName()));
		}		
	}

	@Override
	public void updateModel(SiteDTO site) {
		for(CheckBox checkBox : checkBoxes) {
			site.set(checkBox.getName(), checkBox.getValue());
		}
	}
}
