package org.sigmah.client.page.entry.editor;

import java.util.Map;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.entry.editor.LocationListView.LocationSelectListener;
import org.sigmah.client.page.entry.editor.NewLocationFieldSet.NewLocationPresenter;
import org.sigmah.shared.command.AddLocation;
import org.sigmah.shared.command.GetLocations;
import org.sigmah.shared.command.GetLocations.LocationsResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.LocationDTO2;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
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
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LocationContainer extends LayoutContainer implements NewLocationPresenter, LocationSelectListener {

	private ActivityDTO currentActivity;
	private LocationListView locations;
	private MapFieldSet map;
	private Dispatcher service;
	private EventBus eventBus;
	private MapPresenter mapPresenter;
	private AdminFieldSetPresenter adminPresenter;
	private SiteDTO site;
	private AdminFieldSet adminFieldSet;
	private NewLocationFieldSet fieldsetNewLocation;
	private Window windowAddLocation;

	public LocationContainer(Dispatcher service, EventBus eventBus, ActivityDTO activity) {
		this.service=service;
		this.eventBus=eventBus;
		this.currentActivity=activity;
		
		initializeComponent();
	}

	private void initializeComponent() {
		HBoxLayout layout = new HBoxLayout();
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.STRETCH);
		setLayout(layout);
		HBoxLayoutData dataAdminFields = new HBoxLayoutData();
		
		adminFieldSet = new AdminFieldSet(currentActivity);
		adminFieldSet.setBorders(false);
		add(adminFieldSet, dataAdminFields);
		
		createWindowAddLocation();
		
		createMap();
		createLocationList();
		add(map);
		
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

	private void createWindowAddLocation() {
		windowAddLocation = new Window();
		windowAddLocation.setWidth(400);
		windowAddLocation.setHeight(400);
		windowAddLocation.setLayout(new FitLayout());
		windowAddLocation.add(new NewLocationFieldSet(this));
		windowAddLocation.setTitle(I18N.CONSTANTS.addLocation());
	}

	protected void getLocations() {
		GetLocations getLocations = new GetLocations()
			.setName(adminFieldSet.getLocationName())
			.setAdminEntityIds(adminPresenter.getAdminEntityIds());
		
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
				locations.show(result);
				map.setLocations(result.getLocations());
			}
		});
	}

	private void createMap() {
		map = new MapFieldSet(currentActivity.getDatabase().getCountry(), this);
		map.setBorders(false);
	}

	private void createLocationList() {
		LayoutContainer locationContainer = new LayoutContainer();
		locationContainer.setBorders(false);
		locationContainer.setWidth("300px");
		VBoxLayout layout = new VBoxLayout();
		layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		locationContainer.setLayout(layout);
		
		Button buttonAddLocation = new Button(I18N.CONSTANTS.addLocation());
		buttonAddLocation.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
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
		});
		
		FieldSet fieldsetLocations = new FieldSet();
		fieldsetLocations.setHeading("Choose existing location");
		locations = new LocationListView(this); 
		fieldsetLocations.add(locations);
		
		fieldsetNewLocation = new NewLocationFieldSet(this);
				
		VBoxLayoutData vbldExistingLocations = new VBoxLayoutData();
		vbldExistingLocations.setFlex(1.0);
		
		locationContainer.add(fieldsetLocations, vbldExistingLocations);

		VBoxLayoutData vbldButton = new VBoxLayoutData();
		vbldButton.setMargins(new Margins(5));
		locationContainer.add(buttonAddLocation, vbldButton);
		add(locationContainer);
		fieldsetLocations.setBorders(false);
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
	
	@Override
	public void onAdd(final LocationDTO2 lcoation) {
		//TODO: set adminentities
		lcoation.setLocationTypeId(1);
		service.execute(new AddLocation().setLocation(lcoation), fieldsetNewLocation.getMonitor(), new AsyncCallback<CreateResult>() {
			@Override
			public void onFailure(Throwable caught) { 
				//TODO: handle failure
			}

			@Override
			public void onSuccess(CreateResult result) {
				lcoation.setId(result.getNewId());
				locations.add(lcoation);
				windowAddLocation.hide();
				site.setLocation(lcoation);
				Info.display("Added location", "Location [" + lcoation.getName() + "] added");
			}
		});
	}

	@Override
	public void onSelectLocation(LocationDTO2 location) {
		map.setLocationSelected(location);
		locations.setSelectedLocation(location); 
	}
	
}
