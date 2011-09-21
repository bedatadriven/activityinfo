package org.sigmah.client.page.entry.editor;

import java.util.Collections;
import java.util.List;

import org.sigmah.shared.command.GetLocations.LocationsResult;
import org.sigmah.shared.dto.LocationDTO2;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;

public class LocationListView extends ListView<LocationDTO2> {

	private ListStore<LocationDTO2> store = new ListStore<LocationDTO2>();
	
	public LocationListView() {
		super();
		
		setWidth(200);
		setStore(store);
		setDisplayProperty("name");
		setHeight("100%");
	}


	public void show(LocationsResult result) {
		store.removeAll();
		LocationDTO2 locationNoResult = new LocationDTO2();
		List<LocationDTO2> locations = null;
		if (result.isHasExceededTreshold()) {
			locationNoResult.setName("Exceeded threshold");
			locations = Collections.singletonList(locationNoResult);
		} else if (result.getLocations().size() == 0) {
			locationNoResult.setName("No results");
			locations = Collections.singletonList(locationNoResult);
		}else {
			locations = result.getLocations();
		}
		store.add(locations);
	}
}
