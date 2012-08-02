/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.config.design;


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

        NumberField idField = new NumberField();
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
        
        binding.addFieldBinding(new MappingComboBoxBinding(locationTypeCombo, "locationTypeId"));
        
        final MappingComboBox frequencyCombo = new MappingComboBox();
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

        binding.addListener(Events.Bind, new Listener<BindingEvent>() {

			@Override
			public void handleEvent(BindingEvent be) {
				locationTypeCombo.setEnabled( !isSaved(be.getModel()));
				frequencyCombo.setEnabled( !isSaved(be.getModel()) );
			}	
		});
        
        this.add(publishedCombo);
    }

    @Override
    public FormBinding getBinding() {
        return binding;
    }


	private boolean isSaved(ModelData model) {
		return model.get("id") != null;
	}
}
