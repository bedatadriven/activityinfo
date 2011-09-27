package org.sigmah.client.page.config.locations;

import java.util.List;
import java.util.Map;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.columns.ReadTextColumn;
import org.sigmah.shared.command.ExactMatchingLocations;
import org.sigmah.shared.command.ExactMatchingLocations.ExactMatchingLocationsResult;
import org.sigmah.shared.dto.LocationDTO2;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ExactMatches extends LayoutContainer {
	private Grid<LocationDTO2> gridUniqueLocations;
	private Grid<LocationDTO2> gridMatches;
	private ListStore<LocationDTO2> storeUniqueLocations = new ListStore<LocationDTO2>();
	private ListStore<LocationDTO2> storeMatches = new ListStore<LocationDTO2>();
	private Map<LocationDTO2, List<LocationDTO2>> locationsByLocation = Maps.newHashMap();
	private Dispatcher service;
	private EventBus eventBus;
	
	public ExactMatches(EventBus eventBus, Dispatcher service) {
		super();
		
		this.eventBus=eventBus;
		this.service=service;
		
		initializeComponent();
		
		createUniqueLocationsGrid();
		createMatchesGrid();
		
		getExactMatchingLocations();
	}

	private void getExactMatchingLocations() {
		service.execute(new ExactMatchingLocations(), null, new AsyncCallback<ExactMatchingLocationsResult>() {
			@Override
			public void onFailure(Throwable caught) {
				//TODO: Handle failure
			}

			@Override
			public void onSuccess(ExactMatchingLocationsResult result) {
				List<LocationDTO2> uniqueLocations = Lists.newArrayList();
					for (List<LocationDTO2> locations : result.getMatchingLocations()) {
						LocationDTO2 firstLocation = locations.get(0);
					uniqueLocations.add(firstLocation);
					locationsByLocation.put(firstLocation, locations);
				}
			}
		});
	}

	private void createMatchesGrid() {
		List<ColumnConfig> configs = Lists.newArrayList();
		configs.add(new ReadTextColumn("name", I18N.CONSTANTS.name(), 100));
		configs.add(new ReadTextColumn("axe", I18N.CONSTANTS.axe(), 100));
		configs.add(new ReadTextColumn("id", "id", 40));
		configs.add(new ReadTextColumn("latitude", I18N.CONSTANTS.lat(), 50));
		configs.add(new ReadTextColumn("longitude", I18N.CONSTANTS.longitude(), 50));
		configs.add(new ReadTextColumn("type", I18N.CONSTANTS.locationType(), 50));
		gridMatches = new Grid<LocationDTO2>(storeMatches, new ColumnModel(configs));
	}

	private void createUniqueLocationsGrid() {
		List<ColumnConfig> configs = Lists.newArrayList();
		configs.add(new ReadTextColumn("name", I18N.CONSTANTS.name(), 100));
		configs.add(new ReadTextColumn("axe", I18N.CONSTANTS.axe(), 100));
		configs.add(new ReadTextColumn("id", "id", 40));
		configs.add(new ReadTextColumn("latitude", I18N.CONSTANTS.lat(), 50));
		configs.add(new ReadTextColumn("longitude", I18N.CONSTANTS.longitude(), 50));
		configs.add(new ReadTextColumn("type", I18N.CONSTANTS.locationType(), 50));
		gridUniqueLocations = new Grid<LocationDTO2>(storeUniqueLocations, new ColumnModel(configs));
		gridUniqueLocations.getSelectionModel().addListener(Events.Select, new SelectionChangedListener<LocationDTO2>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<LocationDTO2> se) {
				storeMatches.removeAll();
				storeMatches.add(locationsByLocation.get(se));
			}
		});
	}

	private void initializeComponent() {
		HBoxLayout layout = new HBoxLayout();
		setLayout(layout);
		
	}
	
}
