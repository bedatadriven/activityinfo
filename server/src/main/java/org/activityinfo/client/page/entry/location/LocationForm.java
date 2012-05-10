package org.activityinfo.client.page.entry.location;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.offline.command.handler.KeyGenerator;
import org.activityinfo.client.page.entry.admin.AdminComboBox;
import org.activityinfo.client.page.entry.admin.AdminFieldSetPresenter;
import org.activityinfo.client.page.entry.admin.AdminSelectionChangedEvent;
import org.activityinfo.client.page.entry.admin.BoundsChangedEvent;
import org.activityinfo.client.widget.CoordinateFields;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.report.content.AiLatLng;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.user.client.Timer;

public class LocationForm extends LayoutContainer {
	
	private static final int LABEL_WIDTH = 100;
	private static final int FIELD_WIDTH = 150;
	private static final int BUTTON_SPACE = 5;
	
	private TextField<String> nameField;
	private TextField<String> axeField;
	
	private Timer nameTypeAheadTimer;
	
	private LocationSearchPresenter searchPresenter;
	private NewLocationPresenter newLocationPresenter;
	private AdminFieldSetPresenter adminPresenter;
	
	private CoordinateFields coordinateFields;
	
	private LayoutContainer newFormButtonContainer;
	private SearchAdminComboBoxSet comboBoxes;
	
	private int locationTypeId;
	
	
	public LocationForm(Dispatcher dispatcher, int locationTypeId, 
			final LocationSearchPresenter searchPresenter, 
			final NewLocationPresenter newLocationPresenter) {
		this.searchPresenter = searchPresenter;
		this.newLocationPresenter = newLocationPresenter;
		this.locationTypeId = locationTypeId;
		
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(LABEL_WIDTH);
		layout.setDefaultWidth(FIELD_WIDTH);
		setLayout(layout);
		setStyleAttribute("marginLeft", "8px");
	
		addAdminCombos(dispatcher, searchPresenter);	
		addNameField();
		addAxeField();
		addCoordFields();
		addNewLocationButtons();	
		
		adminPresenter.addListener(AdminSelectionChangedEvent.TYPE, new Listener<AdminSelectionChangedEvent>() {
			@Override
			public void handleEvent(AdminSelectionChangedEvent be) {
				search();
				coordinateFields.validate();
			}
		});
		
		newLocationPresenter.addListener(NewLocationPresenter.ACTIVE_STATE_CHANGED, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				setNewFormActive(newLocationPresenter.isActive());
			}
		});
		
		adminPresenter.addListener(BoundsChangedEvent.TYPE, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				coordinateFields.setBounds(adminPresenter.getBoundsName(), adminPresenter.getBounds());
				newLocationPresenter.setBounds(adminPresenter.getBounds());
			}
		});
		
		nameTypeAheadTimer = new Timer() {
			
			@Override
			public void run() {
				search();
			}
		};
	}

	private void addNameField() {
		nameField = new TextField<String>();
		nameField.setFieldLabel(I18N.CONSTANTS.searchLocation());
		nameField.setMaxLength(50);
		add(nameField);
		
		nameField.addKeyListener(new KeyListener() {

			@Override
			public void componentKeyDown(ComponentEvent event) {
				nameTypeAheadTimer.schedule(200);
			}
		});
	}
	
	private void addAxeField() {
		axeField = new TextField<String>();
		axeField.setFieldLabel(I18N.CONSTANTS.axe());
		axeField.setMaxLength(50);
		add(axeField);
	}

	private void addAdminCombos(Dispatcher dispatcher,
			final LocationSearchPresenter searchPresenter) {
		adminPresenter = new AdminFieldSetPresenter(dispatcher, 
				searchPresenter.getCountry(), searchPresenter.getCountry().getAdminLevels());
		
		comboBoxes = new SearchAdminComboBoxSet(this, adminPresenter);
		
		for(AdminComboBox comboBox : comboBoxes) {
			add(comboBox.asWidget());
		}
	}
	
	private void addCoordFields() {
		coordinateFields = new CoordinateFields();
		coordinateFields.setToolTip(I18N.CONSTANTS.coordinateToolTip());

		add(coordinateFields.getLatitudeField());
		add(coordinateFields.getLongitudeField());
				
		newLocationPresenter.addListener(NewLocationPresenter.POSITION_CHANGED, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				if(!newLocationPresenter.isProvisional()) {
					coordinateFields.setValue(newLocationPresenter.getLatLng());
				}
			}
		});
		coordinateFields.addChangeListener(new Listener<FieldEvent>() {
			
			@Override
			public void handleEvent(FieldEvent be) {
				AiLatLng value = coordinateFields.getValue();
				if(value != null) {
					newLocationPresenter.setLatLng(value);
				}
				
			}
		});
	}

	private void addNewLocationButtons() {

		int buttonWidth = (FIELD_WIDTH-BUTTON_SPACE) / 2;

		Button saveButton = new Button(I18N.CONSTANTS.useNewLocation(), IconImageBundle.ICONS.save(), new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				saveNewLocation();
			}
		});
		saveButton.setWidth(buttonWidth);
		
		Button cancelButton = new Button(I18N.CONSTANTS.cancel(), new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				newLocationPresenter.setActive(false);
			}
		});
		cancelButton.setWidth(buttonWidth);
				
		newFormButtonContainer = new LayoutContainer();
		newFormButtonContainer.setWidth(FIELD_WIDTH);
		newFormButtonContainer.setLayout(new HBoxLayout());
		newFormButtonContainer.add(saveButton);
		newFormButtonContainer.add(cancelButton, new HBoxLayoutData(0, 0, 0, BUTTON_SPACE));
		
		add(newFormButtonContainer, buttonLayout());
		
		setNewFormActive(false);
	}

	private FormData buttonLayout() {
		FormData containerLayout = new FormData();
		containerLayout.setMargins(new Margins(0, 0, 0, LABEL_WIDTH + BUTTON_SPACE));
		return containerLayout;
	}

	private void saveNewLocation() {
		if(coordinateFields.validate() && nameField.validate()) {
			LocationDTO newLocation = new LocationDTO();
			newLocation.setId(new KeyGenerator().generateInt());
			newLocation.setLocationTypeId(locationTypeId);
			newLocation.setName(nameField.getValue());
			newLocation.setAxe(axeField.getValue());
			newLocation.setLatitude(coordinateFields.getLatitudeField().getValue());
			newLocation.setLongitude(coordinateFields.getLongitudeField().getValue());
			for(AdminLevelDTO level : adminPresenter.getAdminLevels()) {
				newLocation.setAdminEntity(level.getId(), adminPresenter.getAdminEntity(level));
			}
			newLocationPresenter.accept(newLocation);
		}
	}

	private void setNewFormActive(boolean active) {
		nameField.setFieldLabel(active ? I18N.CONSTANTS.location() : I18N.CONSTANTS.searchLocation());
		axeField.setVisible(active);
		coordinateFields.setVisible(active);
		newFormButtonContainer.setVisible(active);
		newFormButtonContainer.layout(true);
		comboBoxes.setMode(active ? EditMode.NEW_LOCATION : EditMode.SEARCH);
		
		nameField.setAllowBlank(!active);
		nameField.setEmptyText(active ? null : I18N.CONSTANTS.typeToFilter());
		
		layout(true);
	}
	
	private void search() {
		searchPresenter.search(nameField.getRawValue(), 
				adminPresenter.getAdminEntityIds(), adminPresenter.getBounds());
	}
}
