/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.config.design;


import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.page.common.widget.MappingComboBox;
import org.sigmah.client.page.common.widget.MappingComboBoxBinding;
import org.sigmah.shared.dto.IndicatorDTO;

class IndicatorForm extends AbstractDesignForm {

    private FormBinding binding;
	
	public IndicatorForm()  {
        super();

        binding = new FormBinding(this);

		UIConstants constants = GWT.create(UIConstants.class);
		
		this.setLabelWidth(150);
		this.setFieldWidth(200);

		
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
		
		CheckBox collectInterventionField = new CheckBox();
		collectInterventionField.setBoxLabel(constants.duringIntervention());
		binding.addFieldBinding(new FieldBinding(collectInterventionField, "collectIntervention"));

		CheckBox collectMonitoringField = new CheckBox();
		collectMonitoringField.setBoxLabel(constants.duringMonitoring());
        binding.addFieldBinding(new FieldBinding(collectMonitoringField, "collectMonitoring"));
		
		CheckBoxGroup group = new CheckBoxGroup();
		group.setFieldLabel(constants.dataCollection());
		group.setOrientation(Orientation.VERTICAL);
		group.add(collectInterventionField);
		group.add(collectMonitoringField);
		this.add(group);

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
