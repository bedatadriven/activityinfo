package org.sigmah.client.page.entry.form;

import com.extjs.gxt.ui.client.widget.LayoutContainer;

/** Present the user with:
 * - a filter for name & AdminLevels, 
 * - a list of matching locations 
 * - a map showing matched locations
 * - ability to add a new location when there is no matching location */
public class LocationPicker 
	extends 
		LayoutContainer  {
//	
//	private Dispatcher service;
//	private EventBus eventBus;
//
//	private ActivityDTO currentActivity;
//	private LocationDTO selectedLocation;
//	private SiteDTO site;
//
//	private SearchResultListView locations;
//	
//	private SearchResultMapView map;
//	private MapPresenter mapPresenter; 
//
//	private AdminFieldSetPresenter adminPresenter;
//	private AdminComboBoxSet adminFieldSet;
//	private NewLocationFieldSet fieldsetNewLocation;
//	private Window windowAddLocation;
//
//	private LayoutContainer leftContainer;
//	private LayoutContainer locationsAndMapContainer;
//	private MarkerNumbering markers = new MarkerNumbering();
//	private BiMap<Integer, LocationDTO> originals = HashBiMap.<Integer, LocationDTO>create();
//	private TextField<String> nameField;
//	private Timer nameDelay;
//	private static final int delay=250; // milliseconds 
//	private Button buttonUseLocation;
//	private SelectLocationCallback callback;
//	
//	public interface SelectLocationCallback {
//		public void cancel();
//		public void useLocation(LocationDTO location);
//	}
//
//	public LocationPicker(Dispatcher service, EventBus eventBus, ActivityDTO activity, SelectLocationCallback callback) {
//		this.service=service;
//		this.eventBus=eventBus;
//		this.currentActivity=activity;
//		this.callback=callback;
//		
//		initializeComponent();
//
//		// Picker of AdminEntities
//		createAdminFieldSet();
//		createFieldsetNewLocation();
//		
//		// Left & right columns
//		createLocationsAndMapContainer();
//		createWindowAddLocation();
//		createLeftContainer();
//		
//		// Left column
//		createUseLocationButton();
//		createNameTextbox();
//		createLocationList();
//		
//		// Cancel/UseLocation buttons
//		createCancelButton();
//		createAddLocationButton();
//		
//		// Adds it to the right column
//		createMap();
//		
//		createPresenters();
//	}
//	
//	private void createFieldsetNewLocation() {
//		fieldsetNewLocation = new NewLocationFieldSet(this);
//	}
//
//	private void initializeComponent() {
//		VBoxLayout layout = new VBoxLayout();
//		layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
//		setLayout(layout);
//	}
//
//	private void createUseLocationButton() {
//		buttonUseLocation = new Button(I18N.CONSTANTS.useLocation());
//		buttonUseLocation.addSelectionListener(new SelectionListener<ButtonEvent>() {
//			@Override
//			public void componentSelected(ButtonEvent ce) {
//				callback.useLocation(selectedLocation);
//			}
//		});
//		buttonUseLocation.setIcon(IconImageBundle.ICONS.useLocation());
//		leftContainer.add(buttonUseLocation, new VBoxLayoutData(new Margins(5)));
//	}
//
//	private void createCancelButton() {
//		Button buttonCancel = new Button(I18N.CONSTANTS.cancel());
//		buttonCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {
//			@Override
//			public void componentSelected(ButtonEvent ce) {
//				callback.cancel();
//			}
//		});
//		buttonCancel.setIcon(IconImageBundle.ICONS.cancel());
//		leftContainer.add(buttonCancel, new VBoxLayoutData(new Margins(5)));
//	}
//
//	private void createAddLocationButton() {
//		Button buttonAddLocation = new Button(I18N.CONSTANTS.addLocation());
//		buttonAddLocation.addSelectionListener(new SelectionListener<ButtonEvent>() {
//			@Override
//			public void componentSelected(ButtonEvent ce) {
//				proceedToShowAddLocationDialogAfterUserConfirmed();
//			}
//		});
//		leftContainer.add(buttonAddLocation, new VBoxLayoutData(new Margins(5)));
//	}
//
//	private void createLeftContainer() {
//		leftContainer = new LayoutContainer();
//		leftContainer.setBorders(false);
//		leftContainer.setWidth("300px");
//		VBoxLayout layout = new VBoxLayout();
//		layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
//		leftContainer.setLayout(layout);
//		locationsAndMapContainer.add(leftContainer, new HBoxLayoutData());
//	}
//
//	private void createNameTextbox() {
//        nameField = new TextField<String>();
//        nameField.setName("locationName");
//        nameField.setFieldLabel(currentActivity.getLocationType().getName());
//        nameField.setAllowBlank(false);
//        nameField.addKeyListener(new KeyListener() {
//			@Override
//			public void componentKeyUp(ComponentEvent event) {
//				super.componentKeyUp(event);
//				nameDelay.schedule(delay);
//			}
//        });
//    	nameDelay = new Timer() {
//			@Override
//			public void run() {
//				getLocations();
//			}
//		};
//		leftContainer.add(nameField, new VBoxLayoutData(new Margins(5,5,5,5)));
//    }
//
//	private void createPresenters() {
////		adminPresenter = new AdminFieldSetPresenter(service, currentActivity, adminFieldSet);
////		mapPresenter = new MapPresenter(map);
////        adminPresenter.setListener(new AdminFieldSetPresenter.Listener() {
////            @Override
////            public void onBoundsChanged(String name, BoundingBoxDTO bounds) {
////                mapPresenter.setBounds(name, bounds);
////            }
////
////            @Override
////            public void onModified() {
////            	getLocations();
////            }
////        });
//	}
//
//	private void createAdminFieldSet() {
//		VBoxLayoutData dataAdminFields = new VBoxLayoutData();
//		dataAdminFields.setMargins(new Margins(5,2,5,2));
////		adminFieldSet = new AdminComboBoxSet(currentActivity);
////		adminFieldSet.setBorders(false);
////		adminFieldSet.setHeight("50px");
////		add(adminFieldSet, dataAdminFields);
//	}
//
//	private void createLocationsAndMapContainer() {
//		locationsAndMapContainer = new LayoutContainer();
//		HBoxLayout layout = new HBoxLayout();
//		layout.setHBoxLayoutAlign(HBoxLayoutAlign.STRETCH);
//		locationsAndMapContainer.setLayout(layout);
//		VBoxLayoutData data = new VBoxLayoutData();
//		data.setFlex(1.0);
//		add(locationsAndMapContainer, data);
//	}
//
//	private void createWindowAddLocation() {
//		windowAddLocation = new Window();
//		windowAddLocation.setWidth(300);
//		windowAddLocation.setHeight(300);
//		windowAddLocation.setLayout(new FitLayout());
//		windowAddLocation.add(new NewLocationFieldSet(this));
//		windowAddLocation.setHeading(I18N.CONSTANTS.addLocation());
//	}
//
//	/** Gets all locations from database based on the criteria the user has set */
//	protected void getLocations() {
//		SearchLocations getLocations = new SearchLocations()
//			.setName(nameField.getValue())
//			.setAdminEntityIds(adminPresenter.getAdminEntityIds())
//			.setLocationTypeId(currentActivity.getLocationTypeId());
//		
//		for (Integer id : adminPresenter.getAdminEntityIds()) {
//			System.out.println("id: " + id);
//		}
//		service.execute(getLocations, null, new AsyncCallback<LocationsResult>() {
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO: handle failure
//			}
//
//			@Override
//			public void onSuccess(LocationsResult result) {
//				List<SearchResultModel> locationsList = fromLocations(result.getLocations());
//				locations.show(result, locationsList);
//				addAllModelsToMap(result.getLocations());
//				map.setLocations(locationsList);
//			}
//		});
//	}
//	
//	/** Converts a list of LocationDTO's to a list of reflective Locations */
//	private List<SearchResultModel> fromLocations(List<LocationDTO> locations) {
//		markers.reset();
//		List<SearchResultModel> newLocations = new ArrayList<SearchResultModel>();
//		int i=0;
//		if (locations != null) {
//			for(LocationDTO location : locations) {
//				SearchResultModel viewModel = new SearchResultModel(location, markers.next());
//				newLocations.add(viewModel);
//				i++;
//			}
//		}
//		return newLocations;
//	}
//
//	private void createMap() {
//		map = new SearchResultMapView(currentActivity.getDatabase().getCountry(), this);
//		map.setBorders(false);
//		HBoxLayoutData data = new HBoxLayoutData();
//		data.setMargins(new Margins(5,5,5,5));
//		data.setFlex(1.0);
//
//		locationsAndMapContainer.add(map, data);
//	}
//
//	private void createLocationList() {
//		locations = new SearchResultListView(this);
//		locations.setHeight("300px");
//		VBoxLayoutData vbldExistingLocations = new VBoxLayoutData();
//		vbldExistingLocations.setMargins(new Margins(5,5,5,5));
//		leftContainer.add(locations, vbldExistingLocations);
//	}
//
//
//	public Map<String, Object> getAllValues() {
//		return null; //return adminPresenter.getChangeMap();
//	}
//
//	public void setSite(SiteDTO site) {
//		this.site=site;
//		adminPresenter.setSelection(site);
//	}
//	
//	/** Called by view(s) when adding a new location */
//	@Override
//	public void onAdd(final LocationDTO lcoation) {
//		// Ensure AdminEntities are set
////		for (Entry<Integer, AdminEntityDTO> property : adminPresenter.getPropertyMapById().entrySet()) {
////			lcoation.setAdminEntity(property.getKey(), property.getValue());
////		}
//		// Ensure locationType is set
//		lcoation.setLocationTypeId(1);
//		service.execute(new AddLocation().setLocation(lcoation), fieldsetNewLocation.getMonitor(), new AsyncCallback<CreateResult>() {
//			@Override
//			public void onFailure(Throwable caught) { 
//				//TODO: handle failure
//			}
//
//			@Override
//			public void onSuccess(CreateResult result) {
//				lcoation.setId(result.getNewId());
//				SearchResultModel newLocation = new SearchResultModel(lcoation, markers.next());
//				windowAddLocation.hide();
//				site.setLocation(lcoation);
//				locations.add(newLocation);
//				Info.display(I18N.CONSTANTS.addedLocation(), I18N.MESSAGES.addedLocation(newLocation.getName()));
//			}
//		});
//	}
//	
//	/** Adds given list of LocationDTO's to internal mapping by keys */
//	private void addAllModelsToMap(List<LocationDTO> locations) {
//		for (LocationDTO location: locations) {
//			originals.put(location.getId(), location);
//		}
//	}
//
//	/** Any of the child widgets call this method to change the current selected location */
//	@Override
//	public void onSelectLocation(SearchResultModel location) {
//		// Only allow using locations with coords
//		buttonUseLocation.setEnabled(location.hasCoordinates());
//		// Notify map and locations list a selection occured 
//		map.setLocationSelected(location);
//		locations.setSelectedLocation(location); 
//		if (location == null) {
//			selectedLocation = null;
//		} else {
//			selectedLocation=originals.get(location.getId());
//		}
//	}
//	
//	/** Annoy the user by popping up an intermediate dialog asking them if they really
//	 * really really really want to add a location. This is intentional: we're making it 
//	 * harder to add a new location, and easier to pick an existing location. As such, data 
//	 * duplication should decrease and data integrity go up. */
//	private void proceedToShowAddLocationDialogAfterUserConfirmed() {
//		final MessageBox box = new MessageBox();
//		box.setButtons(MessageBox.YESNO);
//		box.setTitle(I18N.CONSTANTS.confirmAddLocation());
//		box.setMessage(I18N.MESSAGES.confirmAddLocation());
//		box.setIcon(MessageBox.WARNING);
//		box.addCallback(new Listener<MessageBoxEvent>() {
//			@Override
//			public void handleEvent(MessageBoxEvent be) {
//				if (be.getButtonClicked().getText().toLowerCase().equals(Dialog.YES)) {
//					windowAddLocation.show();
//				} else {
//					box.close();
//				}
//			}
//		});
//		box.show();
//	}
//	
//	/** Keep track of marking the Location such that the user can identify the individual marker on the map */
//	public class MarkerNumbering {
//		int index = 0;
//		
//		public void reset() {
//			index=0;
//		}
//		
//		public String next() {
//			index++;
//			return Integer.toString(index);
//		}
//	}
}
