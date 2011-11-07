package org.sigmah.client.page.entry.location;

import java.util.Collection;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.SearchLocations;
import org.sigmah.shared.command.result.LocationResult;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.LocationDTO;
import org.sigmah.shared.dto.LocationTypeDTO;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LocationSearchPresenter extends BaseObservable {
	
	private Dispatcher dispatcher;
	private CountryDTO country;
	private LocationTypeDTO locationType;

	private final ListLoader<ListLoadResult<LocationDTO>> loader;
	private final ListStore<LocationDTO> store;
	
	private SearchLocations currentSearch;
	
	public LocationSearchPresenter(Dispatcher dispatcher, CountryDTO country, LocationTypeDTO locationType) {
		this.dispatcher = dispatcher;
		this.country = country;
		this.locationType = locationType;
		
		loader = new BaseListLoader<ListLoadResult<LocationDTO>>(new Proxy());
		store = new ListStore<LocationDTO>(loader);
	}
	
	public CountryDTO getCountry() {
		return country;
	}
	
	public LocationTypeDTO getLocationType() {
		return locationType;
	}

	public ListStore<LocationDTO> getStore() {
		return store;
	}
	
	public void search(String name, Collection<Integer> collection) {
		currentSearch = new SearchLocations()
		.setName(name)
		.setAdminEntityIds(collection)
		.setLocationTypeId(locationType.getId());
	
		loader.load();
	}
	
	private void numberLocations(List<LocationDTO> locations) {
		int number = 0;
		for(LocationDTO location : locations) {
			if(location.hasCoordinates()) {
				location.setMarker(String.valueOf((char)('A' + number)));
				number++;
			}
			if(number >= 26) {
				break;
			}
		} 
	}
	
	private class Proxy extends RpcProxy<PagingLoadResult<LocationDTO>> {

		@Override
		protected void load(Object loadConfig,
				final AsyncCallback<PagingLoadResult<LocationDTO>> callback) {
			
			
			dispatcher.execute(currentSearch, null, new AsyncCallback<LocationResult>() {
				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}
		
				@Override
				public void onSuccess(LocationResult locations) {
					numberLocations(locations.getData());
					callback.onSuccess(locations);
				}
			});
		}
	}

}
