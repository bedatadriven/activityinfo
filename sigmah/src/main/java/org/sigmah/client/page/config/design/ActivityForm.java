/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.config.design;


import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.widget.MappingComboBox;
import org.sigmah.client.widget.MappingComboBoxBinding;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.LocationTypeDTO;
import org.sigmah.shared.dto.Published;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;

/**
 * Form for editing ActivityDTO
 */
class ActivityForm extends AbstractDesignForm {

    private FormBinding binding;

    public ActivityForm(Dispatcher service, UserDatabaseDTO database) {
        super();

        binding = new FormBinding(this);

        this.setHeaderVisible(false);
        this.setScrollMode(Scroll.AUTOY);
        this.setLabelWidth(150);
        this.setBorders(false);

        NumberField idField = new NumberField();
        idField.setFieldLabel("ID");
        idField.setReadOnly(true);
        binding.addFieldBinding(new FieldBinding(idField, "id"));
        add(idField);

        TextField<String> nameField = new TextField<String>();
        nameField.setAllowBlank(false);
        nameField.setFieldLabel(I18N.CONSTANTS.name());
        nameField.setMaxLength(45);
        binding.addFieldBinding(new FieldBinding(nameField, "name"));
        this.add(nameField);

        TextField<String> categoryField = new TextField<String>();
        categoryField.setFieldLabel(I18N.CONSTANTS.category());
        binding.addFieldBinding(new FieldBinding(categoryField, "category"));
        add(categoryField);


        MappingComboBox<Integer> locationTypeCombo = new MappingComboBox<Integer>();
        for (LocationTypeDTO type : database.getCountry().getLocationTypes()) {
            locationTypeCombo.add(type.getId(), type.getName());
        }
        locationTypeCombo.setAllowBlank(false);
        locationTypeCombo.setFieldLabel(I18N.CONSTANTS.locationType());
        binding.addFieldBinding(new MappingComboBoxBinding(locationTypeCombo, "locationTypeId"));
        this.add(locationTypeCombo);

        MappingComboBox frequencyCombo = new MappingComboBox();
        frequencyCombo.setAllowBlank(false);
        frequencyCombo.setFieldLabel(I18N.CONSTANTS.reportingFrequency());
        frequencyCombo.add(ActivityDTO.REPORT_ONCE, I18N.CONSTANTS.reportOnce());
        frequencyCombo.add(ActivityDTO.REPORT_MONTHLY, I18N.CONSTANTS.monthly());
        binding.addFieldBinding(new MappingComboBoxBinding(frequencyCombo, "reportingFrequency"));
        this.add(frequencyCombo);
        
        MappingComboBox publishedCombo = new MappingComboBox();
        publishedCombo.setAllowBlank(false);
        publishedCombo.setFieldLabel(I18N.CONSTANTS.published());
        publishedCombo.add(Published.NOT_PUBLISHED.getIndex(), I18N.CONSTANTS.notPublished());
        publishedCombo.add(Published.ALL_ARE_PUBLISHED.getIndex(), I18N.CONSTANTS.allArePublished());
        binding.addFieldBinding(new MappingComboBoxBinding(publishedCombo, "published"));
        
        this.add(publishedCombo);
    }

    @Override
    public FormBinding getBinding() {
        return binding;
    }

}
