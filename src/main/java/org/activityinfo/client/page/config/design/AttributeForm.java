

package org.activityinfo.client.page.config.design;

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


import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.dto.AttributeDTO;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;

class AttributeForm extends AbstractDesignForm {

    private FormBinding binding;

    public AttributeForm() {

        binding = new FormBinding(this);

        NumberField idField = new NumberField();
        idField.setFieldLabel("ID");
        idField.setReadOnly(true);
        binding.addFieldBinding(new FieldBinding(idField, "id"));
        add(idField);

        TextField<String> nameField = new TextField<String>();
        nameField.setFieldLabel(I18N.CONSTANTS.name());
        nameField.setMaxLength(AttributeDTO.NAME_MAX_LENGTH);
        binding.addFieldBinding(new FieldBinding(nameField, "name"));

        add(nameField);

    }

    @Override
    public FormBinding getBinding() {
        return binding;
    }
}