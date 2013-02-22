

package org.activityinfo.client.page.entry.form;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
