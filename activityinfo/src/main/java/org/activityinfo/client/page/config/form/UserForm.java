package org.activityinfo.client.page.config.form;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import org.activityinfo.shared.dto.PartnerModel;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.i18n.UIConstants;

public class UserForm extends FormPanel {

	private ListStore<PartnerModel> partnerStore; 
    private FormBinding binding;

	public UserForm(UserDatabaseDTO database) {
		
		UIConstants constants = GWT.create(UIConstants.class);

        binding = new FormBinding(this);

		FormLayout layout = new FormLayout();
		layout.setLabelWidth(90);
		this.setLayout(layout);
		
		TextField<String> nameField = new TextField<String>();
		nameField.setFieldLabel(constants.name());
		nameField.setAllowBlank(false);
        binding.addFieldBinding(new FieldBinding(nameField, "name"));
		this.add(nameField);
		
		TextField<String> emailField = new TextField<String>();
		emailField.setFieldLabel(constants.email());
		emailField.setAllowBlank(false);
        binding.addFieldBinding(new FieldBinding(emailField, "email"));
		this.add(emailField);
		
		partnerStore = new ListStore<PartnerModel>();
		partnerStore.add(database.getPartners());
		partnerStore.sort("name", SortDir.ASC);
		
		ComboBox<PartnerModel> partnerCombo = new ComboBox<PartnerModel>();
		partnerCombo.setName("partner");
		partnerCombo.setFieldLabel(constants.partner());
		partnerCombo.setDisplayField("name");
		partnerCombo.setStore(partnerStore);
		partnerCombo.setForceSelection(true);
		partnerCombo.setTriggerAction(TriggerAction.QUERY);
		partnerCombo.setAllowBlank(false);
        binding.addFieldBinding(new FieldBinding(partnerCombo, "partner"));
		this.add(partnerCombo);
	
	}
	
	protected CheckBox createCheckBox(String property, String label) {
		CheckBox box = new CheckBox();
		box.setName(property);
		box.setBoxLabel(label);
		return box;
	}

    public FormBinding getBinding() {
        return binding;
    }
}
