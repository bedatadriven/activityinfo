/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.config.design;


import org.activityinfo.client.widget.MappingComboBox;
import org.activityinfo.client.widget.MappingComboBoxBinding;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.client.i18n.UIConstants;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;

class IndicatorForm extends AbstractDesignForm {

    private FormBinding binding;

    public IndicatorForm()  {
        super();

        binding = new FormBinding(this);

        UIConstants constants = GWT.create(UIConstants.class);

        this.setLabelWidth(150);
        this.setFieldWidth(200);

        NumberField idField = new NumberField();
        idField.setFieldLabel("ID");
        idField.setReadOnly(true);
        binding.addFieldBinding(new FieldBinding(idField, "id"));
        add(idField);

        TextField<String> nameField = new TextField<String>();
        nameField.setFieldLabel(constants.name());
        nameField.setAllowBlank(false);
        nameField.setMaxLength(128);
        binding.addFieldBinding(new FieldBinding(nameField, "name"));
        this.add(nameField);

        TextField<String> categoryField = new TextField<String>();
        categoryField.setName("category");
        categoryField.setFieldLabel(constants.category());
        categoryField.setMaxLength(50);
        binding.addFieldBinding(new FieldBinding(categoryField, "category"));
        this.add(categoryField);

        TextField<String> unitsField = new TextField<String>();
        unitsField.setName("units");
        unitsField.setFieldLabel(constants.units());
        unitsField.setAllowBlank(false);
        unitsField.setMaxLength(15);
        binding.addFieldBinding(new FieldBinding(unitsField, "units"));
        this.add(unitsField);

        MappingComboBox aggregationCombo = new MappingComboBox();
        aggregationCombo.setFieldLabel(constants.aggregationMethod());
        aggregationCombo.add(IndicatorDTO.AGGREGATE_SUM, constants.sum());
        aggregationCombo.add(IndicatorDTO.AGGREGATE_AVG, constants.average());
        aggregationCombo.add(IndicatorDTO.AGGREGATE_SITE_COUNT, constants.siteCount());
        binding.addFieldBinding(new MappingComboBoxBinding(aggregationCombo, "aggregation"));
        this.add(aggregationCombo);


        TextField<String> listHeaderField = new TextField<String>();
        listHeaderField.setFieldLabel(constants.listHeader());
        binding.addFieldBinding(new FieldBinding(listHeaderField,"listHeader"));
        this.add(listHeaderField);

        TextArea descField = new TextArea();
        descField.setFieldLabel(constants.description());
        binding.addFieldBinding(new FieldBinding(descField, "description"));
        this.add(descField);
    }

    @Override
    public FormBinding getBinding() {
        return binding;
    }
}
