package org.activityinfo.client.page.entry.form;

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

import java.util.Map;
import java.util.Map.Entry;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.entry.location.LocationDialog;
import org.activityinfo.client.page.entry.location.LocationDialog.Callback;
import org.activityinfo.client.widget.CoordinateFields;
import org.activityinfo.shared.command.CreateLocation;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LocationSection extends FormSectionWithFormLayout<SiteDTO> implements LocationFormSection {

	private boolean isNew;
	private ActivityDTO activity;
	private LocationDTO location;
	private Dispatcher dispatcher;
	private LabelField nameField;
	private LabelField axeField;
	private CoordinateFields coordinateFields;
	private Map<Integer, LabelField> levelFields;
	
	public LocationSection(Dispatcher dispatcher, ActivityDTO activity) {
		this.dispatcher = dispatcher;
		this.activity = activity;
		
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
		
		Button changeLocation = new Button(I18N.CONSTANTS.changeLocation(), new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				changeLocation();
			}
		});
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
			dispatcher.execute(new CreateLocation(location), new AsyncCallback<VoidResult>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccess(VoidResult result) {
					isNew = false;
					callback.onSuccess(null);
				}
			});
		}
	}


	@Override
	public void updateForm(SiteDTO m) {
		// TODO Auto-generated method stub
		
	}

	
	private void changeLocation() {
		LocationDialog dialog = new LocationDialog(dispatcher, activity.getDatabase().getCountry(),
				activity.getLocationType());
		dialog.show(new Callback() {
			
			@Override
			public void onSelected(LocationDTO location, boolean isNew) {
				updateForm(location, isNew);
			}
		});
	}
	
}
