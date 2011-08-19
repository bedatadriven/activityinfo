package org.sigmah.client.page.map.mapOptions;

import com.google.gwt.event.shared.EventHandler;

public interface BasemapChangedEventHandler extends EventHandler {
	public void onBaseMapChanged(BaseMapChangedEvent event);
}
