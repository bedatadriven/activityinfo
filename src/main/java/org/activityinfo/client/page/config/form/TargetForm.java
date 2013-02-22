package org.activityinfo.client.page.config.form;

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

import org.activityinfo.client.i18n.UIConstants;
import org.activityinfo.client.widget.MappingComboBox;
import org.activityinfo.client.widget.MappingComboBoxBinding;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
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
        binding.addFieldBinding(new MappingComboBoxBinding(projectCombo,
            "projectId"));
        this.add(projectCombo);

        MappingComboBox<Integer> partnerCombo = new MappingComboBox<Integer>();
        for (PartnerDTO partner : database.getPartners()) {
            partnerCombo.add(partner.getId(), partner.getName());
        }
        partnerCombo.setAllowBlank(true);
        partnerCombo.setFieldLabel(constants.partner());
        binding.addFieldBinding(new MappingComboBoxBinding(partnerCombo,
            "partnerId"));
        this.add(partnerCombo);
    }

    public FormBinding getBinding() {
        return binding;
    }

}
