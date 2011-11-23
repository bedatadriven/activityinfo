/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.form;

import java.util.List;

import org.sigmah.client.page.entry.form.field.AttributeCheckBoxGroup;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.google.common.collect.Lists;

public class AttributeSection extends FormSectionWithFormLayout<SiteDTO> {

	private List<AttributeCheckBoxGroup> groups = Lists.newArrayList();
	
    public AttributeSection(ActivityDTO activity) {
    	
		for(AttributeGroupDTO attributeGroup : activity.getAttributeGroups()) {

			AttributeCheckBoxGroup boxGroup = new AttributeCheckBoxGroup(attributeGroup);
			boxGroup.setStyleAttribute("marginBottom", "10px");
            boxGroup.setStyleAttribute("width", "100%");  // if the width is specified in px, IE6 flips out 

            add(boxGroup);
            groups.add(boxGroup);
        }
    }

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void updateModel(SiteDTO m) {
		for(AttributeCheckBoxGroup group : groups) {
			for(CheckBox checkBox : group.getValues()) {
				m.set(checkBox.getName(), checkBox.getValue());
			}
		}
	}

	@Override
	public void updateForm(SiteDTO m) {
		for(AttributeCheckBoxGroup group : groups) {
			for(CheckBox checkBox : group.getValues()) {
				checkBox.setValue((Boolean) m.get(checkBox.getName()));
			}		
		}
	}

}
