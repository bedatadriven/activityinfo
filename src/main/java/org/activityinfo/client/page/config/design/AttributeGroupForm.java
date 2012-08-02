/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.config.design;


import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.widget.MappingComboBox;
import org.activityinfo.client.widget.MappingComboBoxBinding;
import org.activityinfo.shared.dto.AttributeGroupDTO;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
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
        binding.addFieldBinding(new MappingComboBoxBinding(typeField, "multipleAllowed"));
        add(typeField);
    }

    @Override
    public FormBinding getBinding() {
        return binding;
    }
}
