package org.activityinfo.client.page.config.design;


import org.activityinfo.client.Application;
import org.activityinfo.client.common.widget.MappingComboBox;
import org.activityinfo.client.common.widget.MappingComboBoxBinding;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.binding.FieldBinding;

public class AttributeForm extends AbstractDesignForm {

    private FormBinding binding;

    public AttributeForm() {

        binding = new FormBinding(this);

		TextField<String> nameField = new TextField<String>();
		nameField.setFieldLabel(Application.CONSTANTS.name());
		binding.addFieldBinding(new FieldBinding(nameField, "name"));

		add(nameField);

	}

    @Override
    public FormBinding getBinding() {
        return binding;
    }
}