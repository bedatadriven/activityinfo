package org.sigmah.client.page.entry.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.entry.editor.LocationListView.LocationSelectListener;
import org.sigmah.client.page.entry.editor.NewLocationFieldSet.NewLocationPresenter;
import org.sigmah.shared.command.AddLocation;
import org.sigmah.shared.command.GetLocations;
import org.sigmah.shared.command.GetLocations.LocationsResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.LocationDTO2;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/** Present the user with:
 * - a filter for name & AdminLevels, 
 * - a list of matching locations 
 * - a map showing matched locations
 * - ability to add a new location when there is no matching location */
public class LocationPicker 
	extends 
		LayoutContainer 
	implements 
		NewLocationPresenter, LocationSelectListener {
	private Dispatcher service;
	private EventBus eventBus;

	private ActivityDTO currentActivity;
	private LocationDTO2 selectedLocation;
	private SiteDTO site;

	private LocationListView locations;
	
	private MapFieldSet map;
	private MapPresenter mapPresenter;

	private AdminFieldSetPresenter adminPresenter;
	private AdminFieldSet adminFieldSet;
	private NewLocationFieldSet fieldsetNewLocation;
	private Window windowAddLocation;

	private LayoutContainer leftContainer;
	private LayoutContainer locationsAndMapContainer;
	private MarkerNumbering markers = new MarkerNumbering();
	private BiMap<Integer, LocationDTO2> originals = HashBiMap.<Integer, LocationDTO2>create();
	private TextField<String> nameField;
	private Timer nameDelay;
	private static final int delay=250; // milliseconds 
	private Button buttonUseLocation;
	private SelectLocationCallback callback;
	
	public interface SelectLocationCallback {
		public void cancel();
		public void useLocation(LocationDTO2 location);
	}

	public LocationPicker(Dispatcher service, EventBus eventBus, ActivityDTO activity, SelectLocationCallback callback) {
		this.service=service;
		this.eventBus=eventBus;
		this.currentActivity=activity;
		this.callback=callback;
		
		initializeComponent();

		// Picker of AdminEntities
		createAdminFieldSet();
		
		// Left & right columns
		createLocationsAndMapContainer();
		createWindowAddLocation();
		createLeftContainer();
		
		// Left column
		createUseLocationButton();
		createNameTextbox();
		createLocationList();
		
		// Cancel/UseLocation buttons
		createCancelButton();
		createAddLocationButton();
		
		// Adds it to the right column
		createMap();
		
		createPresenters();
	}
	
	private void initializeComponent() {
		VBoxLayout layout = new VBoxLayout();
		layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		setLayout(layout);
	}

	private void createUseLocationButton() {
		buttonUseLocation = new Button("Use location");
		buttonUseLocation.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				useLocation();
			}
		});
		buttonUseLocation.setIcon(IconImageBundle.ICONS.useLocation());
		leftContainer.add(buttonUseLocation, new VBoxLayoutData(new Margins(5)));
	}

	private void createCancelButton() {
		Button buttonCancel = new Button(I18N.CONSTANTS.cancel());
		buttonCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				cancel();
			}
		});
		buttonCancel.setIcon(IconImageBundle.ICONS.cancel());
		leftContainer.add(buttonCancel, new VBoxLayoutData(new Margins(5)));
	}

	private void createAddLocationButton() {
		Button buttonAddLocation = new Button(I18N.CONSTANTS.addLocation());
		buttonAddLocation.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				proceedToShowAddLocationDialogAfterUserConfirmed();
			}
		});
		leftContainer.add(buttonAddLocation, new VBoxLayoutData(new Margins(5)));
	}

	private void createLeftContainer() {
		leftContainer = new LayoutContainer();
		leftContainer.setBorders(false);
		leftContainer.setWidth("300px");
		VBoxLayout layout = new VBoxLayout();
		layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		leftContainer.setLayout(layout);
		locationsAndMapContainer.add(leftContainer, new HBoxLayoutData());
	}

	private void createNameTextbox() {
        //if (activity.getLocationType().getBoundAdminLevelId() == null) {
        nameField = new TextField<String>();
        nameField.setName("locationName");
        nameField.setFieldLabel(currentActivity.getLocationType().getName());
        nameField.setAllowBlank(false);
        nameField.addKeyListener(new KeyListener() {
			@Override
			public void componentKeyUp(ComponentEvent event) {
				super.componentKeyUp(event);
				nameDelay.schedule(delay);
			}
        });
    	nameDelay = new Timer() {
			@Override
			public void run() {
				getLocations();
			}
		};
		leftContainer.add(nameField, new VBoxLayoutData(new Margins(5,5,5,5)));
    }

	private void createPresenters() {
		adminPresenter = new AdminFieldSetPresenter(service, currentActivity, adminFieldSet);
		mapPresenter = new MapPresenter(map);
        adminPresenter.setListener(new AdminFieldSetPresenter.Listener() {
            @Override
            public void onBoundsChanged(String name, BoundingBoxDTO bounds) {
                mapPresenter.setBounds(name, bounds);
            }

            @Override
            public void onModified() {
            	getLocations();
            }
        });
	}

	private void createAdminFieldSet() {
		VBoxLayoutData dataAdminFields = new VBoxLayoutData();
		dataAdminFields.setMargins(new Margins(5,2,5,2));
		adminFieldSet = new AdminFieldSet(currentActivity);
		adminFieldSet.setBorders(false);
		adminFieldSet.setHeight("50px");
		add(adminFieldSet, dataAdminFields);
	}

	private void createLocationsAndMapContainer() {
		locationsAndMapContainer = new LayoutContainer();
		HBoxLayout layout = new HBoxLayout();
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.STRETCH);
		locationsAndMapContainer.setLayout(layout);
		VBoxLayoutData data = new VBoxLayoutData();
		data.setFlex(1.0);
		add(locationsAndMapContainer, data);
	}

	private void createWindowAddLocation() {
		windowAddLocation = new Window();
		windowAddLocation.setWidth(300);
		windowAddLocation.setHeight(300);
		windowAddLocation.setLayout(new FitLayout());
		windowAddLocation.add(new NewLocationFieldSet(this));
		windowAddLocation.setHeading(I18N.CONSTANTS.addLocation());
	}

	/** Gets all locations from database based on the criteria the user has set */
	protected void getLocations() {
		GetLocations getLocations = new GetLocations()
			.setName(nameField.getValue())
			.setAdminEntityIds(adminPresenter.getAdminEntityIds())
			.setLocationTypeId(currentActivity.getLocationTypeId());
		
		System.out.println("Getting locations with following id's of adminEntities: ");
		for (Integer id : adminPresenter.getAdminEntityIds()) {
			System.out.println("id: " + id);
		}
		service.execute(getLocations, null, new AsyncCallback<LocationsResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO: handle failure
			}

			@Override
			public void onSuccess(LocationsResult result) {
				List<LocationViewModel> locationsList = fromLocations(result.getLocations());
				locations.show(result, locationsList);
				addAllModelsToMap(result.getLocations());
				map.setLocations(locationsList);
			}
		});
	}
	
	/** Converts a list of LocationDTO's to a list of reflective Locations */
	private List<LocationViewModel> fromLocations(List<LocationDTO2> locations) {
		markers.reset();
		List<LocationViewModel> newLocations = new ArrayList<LocationViewModel>();
		int i=0;
		if (locations != null) {
			for(LocationDTO2 location : locations) {
				LocationViewModel viewModel = new LocationViewModel(location, markers.next());
				newLocations.add(viewModel);
				i++;
			}
		}
		return newLocations;
	}

	private void createMap() {
		map = new MapFieldSet(currentActivity.getDatabase().getCountry(), this);
		map.setBorders(false);
		HBoxLayoutData data = new HBoxLayoutData();
		data.setMargins(new Margins(5,5,5,5));
		data.setFlex(1.0);

		locationsAndMapContainer.add(map, data);
	}

	private void createLocationList() {
		locations = new LocationListView(this);
		locations.setHeight("300px");
		VBoxLayoutData vbldExistingLocations = new VBoxLayoutData();
//		vbldExistingLocations.setFlex(0.5);
		vbldExistingLocations.setMargins(new Margins(5,5,5,5));
		leftContainer.add(locations, vbldExistingLocations);
	}

	protected void useLocation() {
		callback.useLocation(selectedLocation);
	}

	protected void cancel() {
		callback.cancel();
	}

	public Map<String, Object> getChanges() {
		return adminPresenter.getChangeMap();
	}

	public boolean isDirty() {
		return adminPresenter.isDirty();
	}

	public Map<String, Object> getAllValues() {
		return adminPresenter.getChangeMap();
	}

	public void setSite(SiteDTO site) {
		this.site=site;
		adminPresenter.setSite(site);
	}
	
	/** Called by view(s) when adding a new location */
	@Override
	public void onAdd(final LocationDTO2 lcoation) {
		// Ensure AdminEntities are set
		for (Entry<Integer, AdminEntityDTO> property : adminPresenter.getPropertyMapById().entrySet()) {
			lcoation.setAdminEntity(property.getKey(), property.getValue());
		}
		// Ensure locationType is set
		lcoation.setLocationTypeId(1);
		service.execute(new AddLocation().setLocation(lcoation), fieldsetNewLocation.getMonitor(), new AsyncCallback<CreateResult>() {
			@Override
			public void onFailure(Throwable caught) { 
				//TODO: handle failure
			}

			@Override
			public void onSuccess(CreateResult result) {
				lcoation.setId(result.getNewId());
				LocationViewModel newLocation = new LocationViewModel(lcoation, markers.next());
				windowAddLocation.hide();
				site.setLocation(lcoation);
				locations.add(newLocation);
				Info.display("Added location", "Location [" + lcoation.getName() + "] added");
			}
		});
	}
	
	/** Adds given list of LocationDTO's to internal mapping by keys */
	private void addAllModelsToMap(List<LocationDTO2> locations) {
		for (LocationDTO2 location: locations) {
			originals.put(location.getId(), location);
		}
	}

	/** Any of the child widgets call this method to change the current selected location */
	@Override
	public void onSelectLocation(LocationViewModel location) {
		// Only allow using locations with coords
		buttonUseLocation.setEnabled(location.hasCoordinates());
		// Notify map and locations list a selection occured 
		map.setLocationSelected(location);
		locations.setSelectedLocation(location); 
		if (location == null) {
			selectedLocation = null;
		} else {
			selectedLocation=originals.get(location.getId());
		}
	}
	
	/** Annoy the user by popping up an intermediate dialog asking them if they really
	 * really really really want to add a location. This is intentional: we're making it 
	 * harder to add a new location, and easier to pick an existing location. As such, data 
	 * duplication should decrease and data integrity go up. */
	private void proceedToShowAddLocationDialogAfterUserConfirmed() {
		final MessageBox box = new MessageBox();
		box.setButtons(MessageBox.YESNO);
		box.setTitle(I18N.CONSTANTS.confirmAddLocation());
		box.setMessage(I18N.MESSAGES.confirmAddLocation());
		box.setIcon(MessageBox.WARNING);
		box.addCallback(new Listener<MessageBoxEvent>() {
			@Override
			public void handleEvent(MessageBoxEvent be) {
				if (be.getButtonClicked().getText().toLowerCase().equals(Dialog.YES)) {
					windowAddLocation.show();
				} else {
					box.close();
				}
			}
		});
		box.show();
	}
	
	/** Keep track of marking the Location such that the user can identify the individual marker on the map */
	public class MarkerNumbering {
		int index = 0;
		
		public void reset() {
			index=0;
		}
		
		public String next() {
			index++;
			return Integer.toString(index);
		}
	}
}
