package org.activityinfo.client.widget;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.widget.CoordinateField.Axis;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.util.mapping.BoundingBoxDTO;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;

public class CoordinateFields {
	private CoordinateField latitudeField;
	private CoordinateField longitudeField;
	
	public CoordinateFields() {
		longitudeField = new CoordinateField(Axis.LONGITUDE);
		longitudeField.setFieldLabel(I18N.CONSTANTS.longitude());
		
		latitudeField = new CoordinateField(Axis.LONGITUDE);
		latitudeField.setFieldLabel(I18N.CONSTANTS.latitude());
	}
	
	public void setToolTip(String toolTip) {
		longitudeField.setToolTip(toolTip);
		latitudeField.setToolTip(toolTip);
	}
	
	public void setBounds(String name, BoundingBoxDTO bounds) {
		longitudeField.setBounds(name, bounds.getMinLng(), bounds.getMaxLng());
		latitudeField.setBounds(name, bounds.getMinLat(), bounds.getMaxLat());
	}

	public CoordinateField getLatitudeField() {
		return latitudeField;
	}

	public CoordinateField getLongitudeField() {
		return longitudeField;
	}

	public void setValue(AiLatLng latLng) {
		if(latLng  == null) {
			latitudeField.setValue(null);
			longitudeField.setValue(null);
		} else {
			latitudeField.setValue(latLng.getLat());
			longitudeField.setValue(latLng.getLng());
		}
	}
	
	public AiLatLng getValue() {
		if(latitudeField.getValue() == null || longitudeField.getValue() == null) {
			return null;
		} else {
			return new AiLatLng(latitudeField.getValue(), longitudeField.getValue());
		}
	}
	
	public void addChangeListener(final Listener<FieldEvent> listener) {
		Listener<FieldEvent> fieldListener = new Listener<FieldEvent>() {

			@Override
			public void handleEvent(FieldEvent be) {
				listener.handleEvent(be);
			}
		};
		latitudeField.addListener(Events.Change, fieldListener);
		longitudeField.addListener(Events.Change, fieldListener);
	}

	public boolean validate() {
		return latitudeField.validate() && longitudeField.validate();
	}

	public void setVisible(boolean visible) {
		latitudeField.setVisible(visible);
		longitudeField.setVisible(visible);
	}

	public void setReadOnly(boolean readOnly) {
		latitudeField.setReadOnly(readOnly);
		longitudeField.setReadOnly(readOnly);
	}
	
}
