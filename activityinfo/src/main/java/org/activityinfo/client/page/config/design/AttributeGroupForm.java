package org.activityinfo.client.page.config.design;


import org.activityinfo.client.Application;
import org.activityinfo.client.common.widget.MappingComboBox;
import org.activityinfo.client.common.widget.MappingComboBoxBinding;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.binding.FieldBinding;

public class AttributeGroupForm extends AbstractDesignForm {

    private FormBinding binding;

    public AttributeGroupForm() {

        binding = new FormBinding(this);

		TextField<String> nameField = new TextField<String>();
		nameField.setFieldLabel(Application.CONSTANTS.name());
		binding.addFieldBinding(new FieldBinding(nameField, "name"));

		add(nameField);

        MappingComboBox typeField = new MappingComboBox();
        typeField.add(true, Application.CONSTANTS.multipleChoice());
        typeField.add(false, Application.CONSTANTS.singleChoice());
        typeField.setFieldLabel("Type de choix");
        binding.addFieldBinding(new MappingComboBoxBinding(typeField, "multipleAllowed"));
        add(typeField);
	}

    @Override
    public FormBinding getBinding() {
        return binding;
    }
}
