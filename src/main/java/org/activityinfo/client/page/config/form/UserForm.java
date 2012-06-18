/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.config.form;

import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.dto.UserPermissionDTO;
import org.activityinfo.client.i18n.UIConstants;

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

public class UserForm extends FormPanel {

    
	private UserDatabaseDTO database;
	private TextField<String> nameField;
	private TextField<String> emailField;
	private ComboBox<PartnerDTO> partnerCombo;

	public UserForm(UserDatabaseDTO database) {
		this.database = database;
		
		UIConstants constants = GWT.create(UIConstants.class);

		FormLayout layout = new FormLayout();
		layout.setLabelWidth(90);
		this.setLayout(layout);
		
		nameField = new TextField<String>();
		nameField.setFieldLabel(constants.name());
		nameField.setAllowBlank(false);
		this.add(nameField);
		
		emailField = new TextField<String>();
		emailField.setFieldLabel(constants.email());
		emailField.setAllowBlank(false);
		emailField.setRegex("\\S+@\\S+\\.\\S+");
		this.add(emailField);

        ListStore<PartnerDTO> partnerStore = new ListStore<PartnerDTO>();
		partnerStore.add(database.getPartners());
		partnerStore.sort("name", SortDir.ASC);
		
		partnerCombo = new ComboBox<PartnerDTO>();
		partnerCombo.setName("partner");
		partnerCombo.setFieldLabel(constants.partner());
		partnerCombo.setDisplayField("name");
		partnerCombo.setStore(partnerStore);
		partnerCombo.setForceSelection(true);
		partnerCombo.setTriggerAction(TriggerAction.QUERY);
		partnerCombo.setAllowBlank(false);
		this.add(partnerCombo);	
	}
	
	protected CheckBox createCheckBox(String property, String label) {
		CheckBox box = new CheckBox();
		box.setName(property);
		box.setBoxLabel(label);
		return box;
	}


	public UserPermissionDTO getUser() {
		UserPermissionDTO user = new UserPermissionDTO();
		user.setEmail(emailField.getValue());
		user.setName(nameField.getValue());
		user.setPartner(partnerCombo.getValue());
		return user;
	}
}
