package org.activityinfo.client.page.config.form;

import org.activityinfo.shared.i18n.UIConstants;

import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.google.gwt.core.client.GWT;

public class PartnerForm extends FormPanel {

    private FormBinding binding;

	public PartnerForm() {
		super();

        binding = new FormBinding(this);
		
		UIConstants constants = GWT.create(UIConstants.class);
		
		TextField<String> nameField = new TextField<String>();
		nameField.setFieldLabel(constants.name());
		nameField.setMaxLength(16);
		nameField.setAllowBlank(false);
        binding.addFieldBinding(new FieldBinding(nameField, "name"));
		this.add(nameField);
		
		TextField<String> fullField = new TextField<String> ();
		fullField.setFieldLabel(constants.fullName());
		fullField.setMaxLength(64);
        binding.addFieldBinding(new FieldBinding(fullField, "fullName"));
		this.add(fullField);
		
		CheckBox operationalField = new CheckBox();
		operationalField.setName("operational");
		operationalField.setFieldLabel(constants.operational());
		this.add(operationalField);


	}

    public FormBinding getBinding() {
        return binding;
    }
}
