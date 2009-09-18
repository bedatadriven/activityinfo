package org.activityinfo.client.page.config.form;

import org.activityinfo.client.Application;

import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.binding.FieldBinding;

public class DatabaseForm extends FormPanel {

    private FormBinding binding;


	public DatabaseForm() {

        binding = new FormBinding(this);

		TextField<String> nameField = new TextField<String>();
		nameField.setFieldLabel(Application.CONSTANTS.name());
		nameField.setAllowBlank(false);
		nameField.setMaxLength(16);
        binding.addFieldBinding(new FieldBinding(nameField, "name"));
		
		add(nameField);
		
		TextField<String> fullNameField = new TextField<String>();
		fullNameField.setFieldLabel(Application.CONSTANTS.description());
		fullNameField.setMaxLength(50);
        binding.addFieldBinding(new FieldBinding(fullNameField, "fullName"));
		add(fullNameField);

	}

    public FormBinding getBinding() {
        return binding;
    }
}
