/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.entry.form;

import java.util.List;

import org.activityinfo.client.page.entry.form.field.AttributeCheckBoxGroup;
import org.activityinfo.client.page.entry.form.field.AttributeCombo;
import org.activityinfo.client.page.entry.form.field.AttributeField;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.google.common.collect.Lists;

public class AttributeSection extends FormSectionWithFormLayout<SiteDTO> {

	private List<AttributeField> groups = Lists.newArrayList();
	
    public AttributeSection(ActivityDTO activity) {
    	
		for(AttributeGroupDTO attributeGroup : activity.getAttributeGroups()) {

			if(attributeGroup.isMultipleAllowed()) {
				
				AttributeCheckBoxGroup boxGroup = new AttributeCheckBoxGroup(attributeGroup);
				boxGroup.setStyleAttribute("marginBottom", "10px");
	            boxGroup.setStyleAttribute("width", "100%");  // if the width is specified in px, IE6 flips out 
	
	            add(boxGroup);
	            groups.add(boxGroup);	
			
			} else {
				AttributeCombo combo = new AttributeCombo(attributeGroup);
				add(combo);
				groups.add(combo);
			}
        }
    }

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void updateModel(SiteDTO site) {
		for(AttributeField group : groups) {
			group.updateModel(site);
		}
	}

	@Override
	public void updateForm(SiteDTO m) {
		for(AttributeField group : groups) {
			group.updateForm(m);
		}
	}
}
