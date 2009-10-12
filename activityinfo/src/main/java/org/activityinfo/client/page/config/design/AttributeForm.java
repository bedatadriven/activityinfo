package org.activityinfo.client.page.config.design;


import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.widget.form.TextField;
import org.activityinfo.client.Application;

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