package org.sigmah.client.page.entry.editor;

import java.util.Collections;
import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.GetLocations.LocationsResult;

import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;

/** Show a list of locations */
public class LocationListView extends ListView<LocationViewModel> {
	private boolean shouldFireEvent = true;
	
	/** Listener to notify selection changes */
	public interface LocationSelectListener { 
		public void onSelectLocation(LocationViewModel location);
	}
	
	private ListStore<LocationViewModel> store = new ListStore<LocationViewModel>();
	
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
				if (shouldFireEvent && (se.getSelectedItem() != null)) {
					listener.onSelectLocation(se.getSelectedItem());
				}
				shouldFireEvent=true;
			}
		});
	}

	/** Clears list of Locations and adds all given locations to the list */
	public void show(LocationsResult result, List<LocationViewModel> locations) {
		store.removeAll();
		if (result.isHasExceededTreshold()) {
			setExceededThreshold(result.getAmountResults());
			return;
		} 
		if (result.getLocations().size() == 0) {
			setNoResultsFound();
			return;
		}

		el().unmask(); // Ensure masks from exceed threshold/no results is gone
		store.add(locations);
	}
	
	/** Masks this listview and informs the user too many locations match given criteria */
	private void setExceededThreshold(int amountResults) {
		el().mask(I18N.MESSAGES.tooManyLocationsFound(Integer.toString(amountResults)));
	}
	
	/** Masks this listview and informs the user no Locations matched given criteria */
	private void setNoResultsFound() {
		el().mask(I18N.MESSAGES.noLocationsFound());
	}
	
	/** Adds given Location to the list of Locations */
	public void add(LocationViewModel location) {
		store.add(location);
	}
	
	/** Sets current selected Location without firing an event */
	public void setSelectedLocation(LocationViewModel location) {
		shouldFireEvent=false;
		LocationViewModel selectedViewModel = null;
		for (LocationViewModel viewModel : store.getModels()) {
			if (viewModel.getId() == location.getId()) {
				selectedViewModel=viewModel;
			}
		}
		getSelectionModel().setSelection(Collections.singletonList(selectedViewModel));
	}
}
