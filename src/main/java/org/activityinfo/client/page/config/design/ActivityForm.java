package org.activityinfo.client.page.config.design;

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

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.widget.MappingComboBox;
import org.activityinfo.client.widget.MappingComboBoxBinding;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LocationTypeDTO;
import org.activityinfo.shared.dto.Published;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BindingEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
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

        final NumberField idField = new NumberField();
        idField.setFieldLabel("ID");
        idField.setReadOnly(true);
        binding.addFieldBinding(new FieldBinding(idField, "id"));
        add(idField);

        TextField<String> nameField = new TextField<String>();
        nameField.setAllowBlank(false);
        nameField.setFieldLabel(I18N.CONSTANTS.name());
        nameField.setMaxLength(ActivityDTO.NAME_MAX_LENGTH);
        binding.addFieldBinding(new FieldBinding(nameField, "name"));
        this.add(nameField);

        TextField<String> categoryField = new TextField<String>();
        categoryField.setFieldLabel(I18N.CONSTANTS.category());
        categoryField.setMaxLength(ActivityDTO.CATEGORY_MAX_LENGTH);
        binding.addFieldBinding(new FieldBinding(categoryField, "category"));
        add(categoryField);

        final MappingComboBox<Integer> locationTypeCombo = new MappingComboBox<Integer>();
        for (LocationTypeDTO type : database.getCountry().getLocationTypes()) {
            locationTypeCombo.add(type.getId(), type.getName());
        }
        locationTypeCombo.setAllowBlank(false);
        locationTypeCombo.setFieldLabel(I18N.CONSTANTS.locationType());
        this.add(locationTypeCombo);

        binding.addFieldBinding(new MappingComboBoxBinding(locationTypeCombo,
            "locationTypeId"));

        final MappingComboBox frequencyCombo = new MappingComboBox();
        frequencyCombo.setAllowBlank(false);
        frequencyCombo.setFieldLabel(I18N.CONSTANTS.reportingFrequency());
        frequencyCombo
            .add(ActivityDTO.REPORT_ONCE, I18N.CONSTANTS.reportOnce());
        frequencyCombo
            .add(ActivityDTO.REPORT_MONTHLY, I18N.CONSTANTS.monthly());
        binding.addFieldBinding(new MappingComboBoxBinding(frequencyCombo,
            "reportingFrequency"));
        this.add(frequencyCombo);

        MappingComboBox publishedCombo = new MappingComboBox();
        publishedCombo.setAllowBlank(false);
        publishedCombo.setFieldLabel(I18N.CONSTANTS.published());
        publishedCombo.add(Published.NOT_PUBLISHED.getIndex(),
            I18N.CONSTANTS.notPublished());
        publishedCombo.add(Published.ALL_ARE_PUBLISHED.getIndex(),
            I18N.CONSTANTS.allArePublished());
        binding.addFieldBinding(new MappingComboBoxBinding(publishedCombo,
            "published"));

        binding.addListener(Events.Bind, new Listener<BindingEvent>() {

            @Override
            public void handleEvent(BindingEvent be) {
                locationTypeCombo.setEnabled(!isSaved(be.getModel()));
                frequencyCombo.setEnabled(!isSaved(be.getModel()));
            }
        });

        this.add(publishedCombo);

        hideFieldWhenNull(idField);
    }

    @Override
    public FormBinding getBinding() {
        return binding;
    }

    private boolean isSaved(ModelData model) {
        return model.get("id") != null;
    }
}
