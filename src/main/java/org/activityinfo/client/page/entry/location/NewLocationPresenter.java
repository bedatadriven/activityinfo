package org.activityinfo.client.page.entry.location;

import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.util.mapping.BoundingBoxDTO;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;

public class NewLocationPresenter extends BaseObservable {

	public static final EventType POSITION_CHANGED = new EventType();
	public static final EventType ACTIVE_STATE_CHANGED = new EventType();
	public static final EventType BOUNDS_CHANGED = new EventType();
	public static final EventType ACCEPTED = new EventType();
	
	private AiLatLng latLng;
	private boolean provisional;
	private boolean active;
	private BoundingBoxDTO bounds;
	
	public NewLocationPresenter(CountryDTO country) {
		provisional = true;
		bounds = country.getBounds();
		latLng = bounds.centroid();
	}
	
	public AiLatLng getLatLng() {
		return latLng;
	}

	public boolean isProvisional() {
		return provisional;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
		fireEvent(ACTIVE_STATE_CHANGED, new BaseEvent(ACTIVE_STATE_CHANGED));
	}

	public void setLatLng(AiLatLng latLng) {
		this.latLng = latLng;
		this.provisional = false;
		fireEvent(POSITION_CHANGED, new BaseEvent(POSITION_CHANGED));
	}
	
	public void setBounds(BoundingBoxDTO bounds) {
		this.bounds = bounds;
		if(!bounds.contains(latLng)) {
			latLng = bounds.centroid();
			provisional = true;
			fireEvent(POSITION_CHANGED, new BaseEvent(POSITION_CHANGED));
		}
		fireEvent(BOUNDS_CHANGED, new BaseEvent(BOUNDS_CHANGED));
	}

	public BoundingBoxDTO getBounds() {
		return bounds;
	}
	
	public void addAcceptedListener(Listener<LocationEvent> listener) {
		addListener(ACCEPTED, listener);
	}
	
	public void accept(LocationDTO location) {
		fireEvent(ACCEPTED, new LocationEvent(ACCEPTED, this, location));
	}

}
