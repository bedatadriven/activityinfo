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

import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class AttributeCombo extends ComboBox<AttributeDTO> implements
    AttributeField {

    public AttributeCombo(AttributeGroupDTO attributeGroup) {
        super();
        String name = attributeGroup.getName();
        if (attributeGroup.isMandatory()) {
            name += "*";
            this.setAllowBlank(false);
        }
        this.setFieldLabel(Format.htmlEncode(name));
        this.setDisplayField("name");
        this.setTriggerAction(TriggerAction.ALL);
        this.setEditable(false);

        ListStore<AttributeDTO> store = new ListStore<AttributeDTO>();
        store.add(attributeGroup.getAttributes());

        setStore(store);
    }

    @Override
    public void updateForm(SiteDTO site) {
        for (AttributeDTO attribute : getStore().getModels()) {
            if (site.getAttributeValue(attribute.getId())) {
                setValue(attribute);
                return;
            }
        }
        setValue(null);
    }

    @Override
    public void updateModel(SiteDTO site) {
        AttributeDTO selected = getValue();

        for (AttributeDTO attribute : getStore().getModels()) {
            site.setAttributeValue(attribute.getId(),
                selected != null && selected.getId() == attribute.getId());
        }
    }
}
