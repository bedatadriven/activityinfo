package org.activityinfo.client.page.config.design;


import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.binding.Converter;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import org.activityinfo.client.Application;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.page.common.widget.MapIconComboBox;
import org.activityinfo.client.page.common.widget.MappingComboBox;
import org.activityinfo.client.page.common.widget.MappingComboBoxBinding;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.LocationTypeModel;
import org.activityinfo.shared.dto.MapIconModel;
import org.activityinfo.shared.dto.UserDatabaseDTO;

public class ActivityForm extends AbstractDesignForm {

    private ListStore<LocationTypeModel> locationTypeStore;
    private FormBinding binding;

    public ActivityForm(CommandService service, UserDatabaseDTO database) {
        super();

        binding = new FormBinding(this);

        this.setHeaderVisible(false);
        this.setScrollMode(Scroll.AUTOY);
        this.setLabelWidth(150);
        this.setBorders(false);

        TextField<String> nameField = new TextField<String>();
        nameField.setAllowBlank(false);
        nameField.setFieldLabel(Application.CONSTANTS.name());
        nameField.setMaxLength(45);
        binding.addFieldBinding(new FieldBinding(nameField, "name"));
        this.add(nameField);

        TextField<String> categoryField = new TextField<String>();
        categoryField.setFieldLabel(Application.CONSTANTS.category());
        binding.addFieldBinding(new FieldBinding(categoryField, "category"));
        add(categoryField);


        MappingComboBox<Integer> locationTypeCombo = new MappingComboBox<Integer>();
        for (LocationTypeModel type : database.getCountry().getLocationTypes()) {
            locationTypeCombo.add(type.getId(), type.getName());
        }
        locationTypeCombo.setAllowBlank(false);
        locationTypeCombo.setFieldLabel(Application.CONSTANTS.locationType());
        binding.addFieldBinding(new MappingComboBoxBinding(locationTypeCombo, "locationTypeId"));
        this.add(locationTypeCombo);

        MappingComboBox frequencyCombo = new MappingComboBox();
        frequencyCombo.setAllowBlank(false);
        frequencyCombo.setFieldLabel(Application.CONSTANTS.reportingFrequency());
        frequencyCombo.add(ActivityModel.REPORT_ONCE, Application.CONSTANTS.reportOnce());
        frequencyCombo.add(ActivityModel.REPORT_MONTHLY, Application.CONSTANTS.monthly());
        binding.addFieldBinding(new MappingComboBoxBinding(frequencyCombo, "reportingFrequency"));
        this.add(frequencyCombo);

        CheckBoxGroup checkBoxes = new CheckBoxGroup();
        checkBoxes.setFieldLabel(Application.CONSTANTS.attributes());
        checkBoxes.setOrientation(Style.Orientation.VERTICAL);
        this.add(checkBoxes);

        CheckBox assessCheckBox = new CheckBox();
        assessCheckBox.setBoxLabel(Application.CONSTANTS.isAssessment());
        assessCheckBox.setToolTip(Application.CONSTANTS.isAssessmentToolTip());
        binding.addFieldBinding(new FieldBinding(assessCheckBox, "assessment"));
        checkBoxes.add(assessCheckBox);

        MapIconComboBox mapIconField = new MapIconComboBox(service);
        mapIconField.setFieldLabel(Application.CONSTANTS.mapIcon());
        FieldBinding mapIconBinding = new FieldBinding(mapIconField, "mapIcon");
        mapIconBinding.setConverter(new Converter() {
            @Override
            public Object convertModelValue(Object value) {
                return value == null ? null : new MapIconModel((String) value);
            }

            @Override
            public Object convertFieldValue(Object value) {
                return ((MapIconModel) value).getId();
            }
        });
        binding.addFieldBinding(mapIconBinding);
        this.add(mapIconField);
    }

    @Override
    public FormBinding getBinding() {
        return binding;
    }

}
