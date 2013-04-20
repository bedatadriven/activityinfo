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
import org.activityinfo.client.widget.MappingComboBox;
import org.activityinfo.client.widget.MappingComboBoxBinding;
import org.activityinfo.shared.dto.AttributeGroupDTO;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;

class AttributeGroupForm extends AbstractDesignForm {

    private FormBinding binding;

    public AttributeGroupForm() {

        binding = new FormBinding(this);

        NumberField idField = new NumberField();
        idField.setFieldLabel("ID");
        idField.setReadOnly(true);
        binding.addFieldBinding(new FieldBinding(idField, "id"));
        add(idField);

        TextField<String> nameField = new TextField<String>();
        nameField.setFieldLabel(I18N.CONSTANTS.name());
        nameField.setMaxLength(AttributeGroupDTO.NAME_MAX_LENGTH);
        binding.addFieldBinding(new FieldBinding(nameField, "name"));

        add(nameField);

        MappingComboBox typeField = new MappingComboBox();
        typeField.add(true, I18N.CONSTANTS.multipleChoice());
        typeField.add(false, I18N.CONSTANTS.singleChoice());
        typeField.setFieldLabel(I18N.CONSTANTS.choiceType());
        binding.addFieldBinding(new MappingComboBoxBinding(typeField,
            "multipleAllowed"));
        add(typeField);

<<<<<<< HEAD
        hideFieldWhenNull(idField);
=======
        CheckBox mandatoryCB = new CheckBox();
        mandatoryCB.setFieldLabel(I18N.CONSTANTS.mandatory());
        binding.addFieldBinding(new FieldBinding(mandatoryCB, "mandatory"));
        this.add(mandatoryCB);
>>>>>>> 6653ee158137cb98a8e53b72d335125dbe967b6e
    }

    @Override
    public FormBinding getBinding() {
        return binding;
    }
}
