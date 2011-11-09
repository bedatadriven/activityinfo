package org.sigmah.client.page.entry.location;

import org.sigmah.shared.dto.LocationDTO;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

public class LocationEvent extends BaseEvent {

	private LocationDTO location;
	
	public LocationEvent(EventType type, Object source, LocationDTO location) {
		super(type);
		setSource(source);
		this.location = location;
	}

	public LocationDTO getLocation() {
		return location;
	}
}
