package org.activityinfo.client.page.entry.location;

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

import java.util.Collection;
import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.command.GetLocations;
import org.activityinfo.shared.command.GetLocations.GetLocationsResult;
import org.activityinfo.shared.command.SearchLocations;
import org.activityinfo.shared.command.result.LocationResult;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.LocationTypeDTO;
import org.activityinfo.shared.util.mapping.Extents;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LocationSearchPresenter extends BaseObservable {

    public static final EventType ACCEPTED = new EventType();

    private Dispatcher dispatcher;
    private CountryDTO country;
    private LocationTypeDTO locationType;

    private final ListLoader<ListLoadResult<LocationDTO>> loader;
    private final ListStore<LocationDTO> store;

    private SearchLocations currentSearch;
    private Extents searchBounds;

    private LocationDTO selection;

    public LocationSearchPresenter(Dispatcher dispatcher, CountryDTO country,
        LocationTypeDTO locationType) {
        this.dispatcher = dispatcher;
        this.country = country;
        this.locationType = locationType;

        loader = new BaseListLoader<ListLoadResult<LocationDTO>>(new Proxy());
        store = new ListStore<LocationDTO>(loader);

        currentSearch = new SearchLocations()
            .setLocationTypeId(locationType.getId());
        loader.load();
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

    public Extents getBounds() {
        return searchBounds;
    }

    public void search(String name, Collection<Integer> collection,
        Extents bounds) {
        searchBounds = bounds;
        currentSearch = new SearchLocations()
            .setName(name)
            .setAdminEntityIds(collection)
            .setLocationTypeId(locationType.getId());

        loader.load();
    }

    private void numberLocations(List<LocationDTO> locations) {
        int number = 0;
        for (LocationDTO location : locations) {
            if (location.hasCoordinates()) {
                location.setMarker(String.valueOf((char) ('A' + number)));
                number++;
            }
            if (number >= 26) {
                break;
            }
        }
    }

    public LocationDTO getSelection() {
        return selection;
    }

    public void select(Object source, LocationDTO newSelection) {
        int currentId = selection == null ? 0 : selection.getId();
        int newId = newSelection == null ? 0 : newSelection.getId();
        if (currentId != newId) {
            this.selection = newSelection;
            fireEvent(Events.Select, new LocationEvent(Events.Select, source,
                newSelection));
        }
    }

    public void accept() {
        // retrieve the full version of this location
        dispatcher.execute(new GetLocations(selection.getId()),
            new AsyncCallback<GetLocationsResult>() {

                @Override
                public void onFailure(Throwable caught) {
                    // TODO !!
                }

                @Override
                public void onSuccess(GetLocationsResult result) {
                    selection = result.getLocation();
                    fireEvent(ACCEPTED, new BaseEvent(ACCEPTED));
                }
            });

    }

    public void addAcceptListener(Listener<BaseEvent> listener) {
        addListener(ACCEPTED, listener);
    }

    private class Proxy extends RpcProxy<PagingLoadResult<LocationDTO>> {

        @Override
        protected void load(Object loadConfig,
            final AsyncCallback<PagingLoadResult<LocationDTO>> callback) {

            final SearchLocations thisSearch = currentSearch;

            dispatcher.execute(thisSearch, new AsyncCallback<LocationResult>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(LocationResult locations) {
                    if (thisSearch.equals(currentSearch)) {
                        numberLocations(locations.getData());
                        callback.onSuccess(locations);
                    }
                }
            });
        }
    }
}
