package org.sigmah.client.page.entry.form;

import java.util.Map;
import java.util.Map.Entry;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.widget.CoordinateFields;
import org.sigmah.shared.command.CreateLocation;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.LocationDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LocationSection extends FormSectionWithFormLayout<SiteDTO> implements LocationFormSection {

	private boolean isNew;
	private LocationDTO location;
	private Dispatcher dispatcher;
	private LabelField nameField;
	private LabelField axeField;
	private CoordinateFields coordinateFields;
	private Map<Integer, LabelField> levelFields;
	
	public LocationSection(Dispatcher dispatcher, ActivityDTO activity) {
		this.dispatcher = dispatcher;
		
		levelFields = Maps.newHashMap();
		for(AdminLevelDTO level : activity.getDatabase().getCountry().getAdminLevels()) {
			LabelField levelField = new LabelField();
			levelField.setFieldLabel(level.getName());
			add(levelField);
			levelFields.put(level.getId(), levelField);
		}

		nameField = new LabelField();
		nameField.setFieldLabel(activity.getLocationType().getName());
		add(nameField);
		
		axeField = new LabelField();
		axeField.setFieldLabel(I18N.CONSTANTS.axe());
		add(axeField);
		
		coordinateFields = new CoordinateFields();
		coordinateFields.setReadOnly(true);
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
		this.isNew = isNew;
		nameField.setValue(location.getName());
		axeField.setValue(location.getAxe());
		
		for(Entry<Integer,LabelField> entry : levelFields.entrySet()) {
			AdminEntityDTO entity = location.getAdminEntity(entry.getKey());
			entry.getValue().setValue( entity == null ? null : entity.getName());
		}
		
		if(location.hasCoordinates()) {
			coordinateFields.getLatitudeField().setValue(location.getLatitude());
			coordinateFields.getLongitudeField().setValue(location.getLongitude());
		} else {
			coordinateFields.setValue(null);
		}
	}

	@Override
	public void updateModel(SiteDTO site) {
		site.setLocationId(location.getId());
	}
	
	@Override
	public void save(final AsyncCallback<Void> callback) {
		if(!isNew) {
			callback.onSuccess(null);
		} else {
			dispatcher.execute(new CreateLocation(location), null, new AsyncCallback<VoidResult>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccess(VoidResult result) {
					callback.onSuccess(null);
				}
			});
		}
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
