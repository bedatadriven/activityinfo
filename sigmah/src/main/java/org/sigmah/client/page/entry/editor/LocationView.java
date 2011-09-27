package org.sigmah.client.page.entry.editor;

import java.util.Map;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.entry.editor.LocationPicker.SelectLocationCallback;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.LocationDTO2;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;

public class LocationView extends LayoutContainer {
	private EventBus eventBus;
	private Dispatcher service;
	
	private ActivityDTO currentActivity;
	private SiteDTO site;
	
	private LocationPicker locationPicker;
	private MapView mapView;

	private FieldSet adminFieldset;
	private FieldSet nameFieldset;
	private Button buttonChangeLocation;
	private LayoutContainer infoContainer;
	private LayoutContainer containerLocationView;
	private CardLayout cardLayout = new CardLayout();
	
	public LocationView(EventBus eventBus, Dispatcher service, ActivityDTO activity) {
		super();
		
		this.eventBus=eventBus;
		this.service=service;
		this.currentActivity=activity;
		
		InitializeComponent();
		
		createLocationViewContainer();  // Container holding all UI to view a location
		createInfoContainer();		    // Container with fieldsets to display name/axe, adminentities
		createChangeLocationButton();		   
		createNameFieldset();
		createAdminFieldset();
		createMapView();
		
		createLocationPicker();	
	}
	
	private void createLocationViewContainer() {
		containerLocationView = new LayoutContainer();
		HBoxLayout layout = new HBoxLayout();
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.STRETCH);
		containerLocationView.setLayout(layout);
		cardLayout.setActiveItem(containerLocationView);
		add(containerLocationView);
	}

	private void createChangeLocationButton() {
		buttonChangeLocation = new Button("Change location...");
		buttonChangeLocation.setIcon(IconImageBundle.ICONS.editLocation());
		buttonChangeLocation.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				cardLayout.setActiveItem(locationPicker);
			}
		});
		infoContainer.add(buttonChangeLocation, new VBoxLayoutData(new Margins(5,5,5,5)));
	}

	private void createLocationPicker() {
		locationPicker = new LocationPicker(service, eventBus, currentActivity, new SelectLocationCallback() {
			@Override
			public void useLocation(LocationDTO2 location) {
				refreshAdminEntities();
				cardLayout.setActiveItem(containerLocationView);
				
			}
			@Override
			public void cancel() {
				cardLayout.setActiveItem(containerLocationView);
			}
		});
		add(locationPicker);
	}

	public void setSite(SiteDTO site) {
		this.site=site;
		refreshAdminEntities();
		locationPicker.setSite(site);
	}

	private void InitializeComponent() {
		setLayout(cardLayout);
	}
	
	private void createInfoContainer() {
		infoContainer = new LayoutContainer();
		VBoxLayout layout = new VBoxLayout();
		layout.setVBoxLayoutAlign(VBoxLayoutAlign.LEFT);
		infoContainer.setLayout(layout);
		containerLocationView.add(infoContainer, new RowData(300, -1));
	}
	
	/** Creates and adds name/axe formfields, uses binding to populate them */
	private void createNameFieldset() {
		nameFieldset = new FieldSet();
		nameFieldset.setLayout(new FormLayout());
		nameFieldset.setHeading(I18N.CONSTANTS.locationDetails());
		
		LabelField labelName = new LabelField();
		labelName.setName("locationName"); // Use binding
		labelName.setFieldLabel(I18N.CONSTANTS.location());
		nameFieldset.add(labelName);
		
		LabelField labelAxe = new LabelField();
		labelAxe.setName("locationAxe");   // Use binding
		labelAxe.setFieldLabel(I18N.CONSTANTS.axe());
		nameFieldset.add(labelAxe);
		
		infoContainer.add(nameFieldset, new VBoxLayoutData(new Margins(5,5,5,5)));
	}

	private void createMapView() {
		// TODO Auto-generated method stub
		
	}
	
	private void refreshAdminEntities() {
		adminFieldset.removeAll();
        for(AdminLevelDTO level : currentActivity.getAdminLevels()) {
            LabelField labelAdminEntity = new LabelField();
            labelAdminEntity.setFieldLabel(level.getName());
            AdminEntityDTO adminEntity = site.getAdminEntity(level.getId());
            // A Site not neceserily has an admin entity for every adminlevel
            if (adminEntity != null) {
                labelAdminEntity.setText(adminEntity.getName());            	
            } else {
            	labelAdminEntity.setText("");
            }
            adminFieldset.add(labelAdminEntity);
        }
	}

	private void createAdminFieldset() {
		adminFieldset = new FieldSet();
		adminFieldset.setLayout(new FormLayout());
		adminFieldset.setHeading(I18N.CONSTANTS.geography());
		infoContainer.add(adminFieldset, new VBoxLayoutData(new Margins(5,5,5,5)));
	}

	public boolean isDirty() {
		return locationPicker.isDirty();
	}

	public Map<? extends String, ? extends Object> getAllValues() {
		return locationPicker.getAllValues();
	}

	public Map<? extends String, ? extends Object> getChanges() {
		return locationPicker.getChanges();
	}

}
