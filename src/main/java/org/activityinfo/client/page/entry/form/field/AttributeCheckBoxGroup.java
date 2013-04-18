package org.activityinfo.client.page.entry.form.field;

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

import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.google.common.collect.Lists;

public class AttributeCheckBoxGroup extends CheckBoxGroup implements AttributeField {
    private boolean mandatory = false;
    private List<CheckBox> checkBoxes;

    public AttributeCheckBoxGroup(AttributeGroupDTO group) {
        assert group != null;
        String name = group.getName();
        if (group.isMandatory()) {
            name += "*";
        }
        this.setFieldLabel(Format.htmlEncode(name));
        this.setOrientation(Orientation.VERTICAL);

        checkBoxes = Lists.newArrayList();
        for (AttributeDTO attrib : group.getAttributes()) {

            CheckBox box = new CheckBox();
            box.setBoxLabel(attrib.getName());
            box.setName(attrib.getPropertyName());

            this.add(box);
            checkBoxes.add(box);
        }
    }

    @Override
    public void updateForm(SiteDTO site) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setValue((Boolean) site.get(checkBox.getName()));
        }
    }

    @Override
    public void updateModel(SiteDTO site) {
        for (CheckBox checkBox : checkBoxes) {
            site.set(checkBox.getName(), checkBox.getValue());
        }
    }

    @Override
    public boolean validate() {
        boolean result = true;
        if (mandatory) {
            boolean hasValue = false;
            for (CheckBox box : checkBoxes) {
                if (Boolean.TRUE.equals(box.getValue())) {
                    hasValue = true;
                }
            }
            result = hasValue;
        }
        return result;
    }

    @Override
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }
}
