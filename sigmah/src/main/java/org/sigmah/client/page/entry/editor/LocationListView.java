package org.sigmah.client.page.entry.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.page.entry.editor.LocationListView.LocationViewModel;
import org.sigmah.shared.command.GetLocations.LocationsResult;
import org.sigmah.shared.dto.LocationDTO2;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;

public class LocationListView extends ListView<LocationViewModel> {
	private boolean shouldFireEvent = true;
	
	/** Listener to notify selection changes */
	public interface LocationSelectListener {
		public void onSelectLocation(LocationDTO2 location);
	}
	
	private ListStore<LocationViewModel> store = new ListStore<LocationViewModel>();
	private Map<Integer, LocationDTO2> originalModels = new HashMap<Integer, LocationDTO2>();
	
	public LocationListView(final LocationSelectListener listener) {
		super();
		
		setWidth(280);
		setStore(store);
		setDisplayProperty("name");
		setHeight("100%");
		LocationResources.INSTANCE.locationStyle().ensureInjected();
		setTemplate(LocationResources.INSTANCE.locationTemplate().getText());
		setItemSelector(".location");
		
		getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<LocationViewModel>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<LocationViewModel> se) {
				if (shouldFireEvent) {
					LocationDTO2 originalLocation = originalModels.get(se.getSelectedItem().getId());
					listener.onSelectLocation(originalLocation);
				}
				shouldFireEvent=true;
			}
		});
	}

	/** Clears list of Locations and adds all given locations to the list */
	public void show(LocationsResult result) {
		store.removeAll();
		originalModels.clear();
		List<LocationDTO2> locations = null;
		if (result.isHasExceededTreshold()) {
			setExceededThreshold(result.getAmountResults());
		} else if (result.getLocations().size() == 0) {
			setNoResultsFound();
		} else {
			el().unmask(); // Ensure masks from exceed threshold/no results is gone
			locations = result.getLocations();
		}
		store.add(fromLocations(locations));
		addAllModelsToMap(result.getLocations());
	}
	
	/** Adds given list of LocationDTO's to internal mapping by keys */
	private void addAllModelsToMap(List<LocationDTO2> locations) {
		for (LocationDTO2 location: locations) {
			originalModels.put(location.getId(), location);
		}
	}
	
	/** Masks this listview and informs the user too many locations match given criteria */
	private void setExceededThreshold(int amountResults) {
		el().mask("Exceeded threshold");
	}
	
	/** Masks this listview and informs the user no Locations matched given criteria */
	private void setNoResultsFound() {
		el().mask("No Results");
	}
	
	/** Adds given Location to the list of Locations */
	public void add(LocationDTO2 location) {
		store.add(new LocationViewModel(location));
	}
	
	/** Sets current selected Location without firing an event */
	public void setSelectedLocation(LocationDTO2 location) {
		shouldFireEvent=false;
		LocationViewModel selectedViewModel = null;
		for (LocationViewModel viewModel : store.getModels()) {
			if (viewModel.getId() == location.getId()) {
				selectedViewModel=viewModel;
			}
		}
		getSelectionModel().setSelection(Collections.singletonList(selectedViewModel));
	}
	
	/** Converts a list of LocationDTO's to a list of reflective Locations */
	private List<LocationViewModel> fromLocations(List<LocationDTO2> locations) {
		List<LocationViewModel> newLocations = new ArrayList<LocationViewModel>();
		if (locations != null) {
			for(LocationDTO2 location : locations) {
				LocationViewModel viewModel = new LocationViewModel(location);
				newLocations.add(viewModel);
			}
		}
		return newLocations;
	}
	
	/** Reflective Location model for display in a GXT ListView */
	public static class LocationViewModel extends BaseModelData {
		public LocationViewModel(LocationDTO2 location) {
			super();
			set("id", location.getId());
			set("name", location.getName()); 
			set("axe", location.getAxe());
			set("longitude", location.getLongitude());
			set("latitude", location.getLatitude());
			set("hasAxe", location.hasAxe());
			set("hasCoordinates", location.hasCoordinates());
		}
		public String getName() {
			return (String)get("name");
		}
		public String getAxe() {
			return (String)get("axe");
		}
		public Double getLatitude() {
			return (Double)get("latitude");
		}
		public Double getLongitude() {
			return (Double)get("longitude");
		}
		public int getId() {
			return (Integer)get("id");
		}
		public String locationType() {
			return (String) get("locationType");
		}
		public boolean hasCoordinates() {
			return (Boolean)get("hasCoordinates");
		}
		public boolean hasAxe() {
			return (Boolean) get("hasAxe");
		}
	}
}
