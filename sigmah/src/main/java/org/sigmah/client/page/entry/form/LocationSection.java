package org.sigmah.client.page.entry.form;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.widget.CoordinateFields;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.LabelField;

public class LocationSection extends FormSection {

	public LocationSection(ActivityDTO activity) {
		
		for(AdminLevelDTO level : activity.getDatabase().getCountry().getAdminLevels()) {
			LabelField levelField = new LabelField();
			levelField.setFieldLabel(level.getName());
			add(levelField);
		}

		LabelField nameField = new LabelField();
		nameField.setFieldLabel(activity.getLocationType().getName());
		add(nameField);
		
		LabelField axeField = new LabelField();
		axeField.setFieldLabel(I18N.CONSTANTS.axe());
		add(axeField);
		
		CoordinateFields coordinateFields = new CoordinateFields();
		add(coordinateFields.getLatitudeField());
		add(coordinateFields.getLongitudeField());
		
		Button changeLocation = new Button("Change Location");
		add(changeLocation);
		
	}
	
}
