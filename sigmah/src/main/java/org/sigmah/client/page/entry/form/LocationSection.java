package org.sigmah.client.page.entry.form;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.widget.CoordinateFields;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.LocationDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LocationSection extends FormSectionWithFormLayout<SiteDTO> implements LocationFormSection {

	private boolean isNew;
	private LocationDTO location;
	
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
		
		Button changeLocation = new Button(I18N.CONSTANTS.changeLocation());
		add(changeLocation);
		
	}

	@Override
	public boolean validate() {
		return true;
	}
	
	@Override
	public void updateForm(LocationDTO location, boolean isNew) {
		this.location = location;
	}


	@Override
	public void updateModel(SiteDTO site) {
		site.setLocationId(location.getId());
	}
	
	@Override
	public void save(AsyncCallback<Void> callback) {
		callback.onSuccess(null);
	}


	@Override
	public void updateForm(SiteDTO m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LocationDTO getLocation() {
		// TODO Auto-generated method stub
		return null;
	}
	
}