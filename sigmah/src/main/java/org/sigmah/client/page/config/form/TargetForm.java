package org.sigmah.client.page.config.form;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.widget.MappingComboBox;
import org.sigmah.client.widget.MappingComboBoxBinding;
import org.sigmah.shared.dto.LocationTypeDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.google.gwt.core.client.GWT;

public class TargetForm extends FormPanel {

    private FormBinding binding;

	public TargetForm(UserDatabaseDTO database) {
		super();

        binding = new FormBinding(this);
		
		UIConstants constants = GWT.create(UIConstants.class);
		
		TextField<String> nameField = new TextField<String>();
		nameField.setFieldLabel(constants.name());
		nameField.setMaxLength(16);
		nameField.setAllowBlank(false);
        binding.addFieldBinding(new FieldBinding(nameField, "name"));
		this.add(nameField);
		
		DateField date1Field = new DateField();
		date1Field.setFieldLabel(constants.fromDate());
		date1Field.setAllowBlank(false);
		binding.addFieldBinding(new FieldBinding(date1Field, "date1"));
		this.add(date1Field);

		DateField date2Field = new DateField();
		date2Field.setFieldLabel(constants.toDate());
		date2Field.setAllowBlank(false);
		binding.addFieldBinding(new FieldBinding(date2Field, "date2"));
		this.add(date2Field);
		
		MappingComboBox<Integer> projectCombo = new MappingComboBox<Integer>();
		projectCombo.setFieldLabel(constants.project());
		projectCombo.setAllowBlank(true);
		for (ProjectDTO projectDTO : database.getProjects()) {
			projectCombo.add(projectDTO.getId(), projectDTO.getName());
        }
        binding.addFieldBinding(new FieldBinding(projectCombo, "projectId"));
        this.add(projectCombo);
        
        
        MappingComboBox<Integer> partnerCombo = new MappingComboBox<Integer>();
        for (PartnerDTO partner : database.getPartners()) {
        	partnerCombo.add(partner.getId(), partner.getName());
        }
        partnerCombo.setAllowBlank(true);
        partnerCombo.setFieldLabel(constants.partner());
        binding.addFieldBinding(new MappingComboBoxBinding(partnerCombo, "partnerId"));
        this.add(partnerCombo);
	}

    public FormBinding getBinding() {
        return binding;
    }

}
