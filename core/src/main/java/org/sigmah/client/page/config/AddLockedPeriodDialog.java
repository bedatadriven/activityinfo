package org.sigmah.client.page.config;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.config.LockedPeriodsPresenter.AddLockedPeriodView;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;

public class AddLockedPeriodDialog extends FormPanel implements
		AddLockedPeriodView {

	private FieldSet fieldsetParentType;
	private RadioGroup radiogroupParentType;
	private Radio radioDatabase;
	private Radio radioActivity;
	private Radio radioProject;
	private LabelField labelDatabase;
	private LabelField labelDatabaseName;
	private LabelField labelProject;
	private LabelField labelActivity;
	
	private HorizontalPanel panelDatabase;
	private HorizontalPanel panelActivity;
	private HorizontalPanel panelProject;
	
	private UserDatabaseDTO userDatabase;
	
	private ComboBox<ProjectDTO> comboboxProjects;
	private ListStore<ProjectDTO> storeProjects;
	private ComboBox<ActivityDTO> comboboxactivities;
	private ListStore<ActivityDTO> storeActivities;
	
	private TextField<String> textfieldName;
	private DateField datefieldFromDate;
	private DateField datefieldToDate;
	private CheckBox checkboxEnabled;
	
	private EventBus eventBus = new SimpleEventBus();
	
	public AddLockedPeriodDialog() {
		super();
		
		initializeComponent();
		
		setStartState();
	}
	
	@Override
	public void setUserDatabase(UserDatabaseDTO userDatabase) {
		this.userDatabase=userDatabase;
		
		labelDatabaseName.setText(userDatabase.getName());
		
		storeProjects.removeAll();
		storeProjects.add(userDatabase.getProjects());
		
		storeActivities.removeAll();
		storeActivities.add(userDatabase.getActivities());
	}

	private void initializeComponent() {
		setWidth(400);
		setHeight(280);
		
		setHeaderVisible(false);
		
		fieldsetParentType = new FieldSet();
		fieldsetParentType.setHeading(I18N.CONSTANTS.type());
		
		comboboxProjects = new ComboBox<ProjectDTO>();		
		storeProjects = new ListStore<ProjectDTO>();
		comboboxProjects.setStore(storeProjects);
		comboboxProjects.setDisplayField("name");
		comboboxProjects.setForceSelection(true);
		comboboxProjects.setTriggerAction(TriggerAction.ALL);
		comboboxProjects.setEditable(false);
		
		comboboxactivities = new ComboBox<ActivityDTO>();
		storeActivities = new ListStore<ActivityDTO>();
		comboboxactivities.setStore(storeActivities);
		comboboxactivities.setDisplayField("name");
		comboboxactivities.setForceSelection(true);
		comboboxactivities.setTriggerAction(TriggerAction.ALL);
		comboboxactivities.setEditable(false);
		
		radiogroupParentType = new RadioGroup();
		radiogroupParentType.setFieldLabel(I18N.CONSTANTS.type());
		
		labelDatabase = new LabelField(I18N.CONSTANTS.database());
		labelDatabase.setWidth(100);
		
		labelDatabaseName = new LabelField();
		
		radioDatabase = new Radio();
		radioDatabase.setFieldLabel(I18N.CONSTANTS.database());
		radioDatabase.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				
			}
		});
		radiogroupParentType.add(radioDatabase);
		panelDatabase = new HorizontalPanel();
		panelDatabase.add(labelDatabase);
		panelDatabase.add(radioDatabase);
		panelDatabase.add(labelDatabaseName);
		fieldsetParentType.add(panelDatabase);
		
		radioActivity = new Radio();
		radioActivity.setFieldLabel(I18N.CONSTANTS.activity());
		radioActivity.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				comboboxactivities.setEnabled(radioActivity.getValue());
			}
		});
		
		labelActivity = new LabelField(I18N.CONSTANTS.activity());
		labelActivity.setWidth(100);
		
		panelActivity = new HorizontalPanel();
		panelActivity.add(labelActivity);
		panelActivity.add(radioActivity);
		panelActivity.add(comboboxactivities);
		fieldsetParentType.add(panelActivity);
		radiogroupParentType.add(radioActivity);
		
		radioProject = new Radio();
		radioProject.setFieldLabel(I18N.CONSTANTS.project());
		radioProject.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				comboboxProjects.setEnabled(radioProject.getValue());
			}
		});
		
		labelProject = new LabelField(I18N.CONSTANTS.project());
		labelProject.setWidth(100);
		
		panelProject = new HorizontalPanel();
		panelProject.add(labelProject);
		panelProject.add(radioProject);
		panelProject.add(comboboxProjects);
		fieldsetParentType.add(panelProject);
		radiogroupParentType.add(radioProject);
		
		add(fieldsetParentType);
		
		textfieldName = new TextField<String>();
		textfieldName.setFieldLabel(I18N.CONSTANTS.name());
		add(textfieldName);

		checkboxEnabled = new CheckBox();
		checkboxEnabled.setFieldLabel("Enabled");
		add(checkboxEnabled);

		datefieldFromDate = new DateField();
		datefieldFromDate.setFieldLabel(I18N.CONSTANTS.fromDate());
		add(datefieldFromDate);

		datefieldToDate = new DateField();
		datefieldToDate.setFieldLabel(I18N.CONSTANTS.toDate());
		add(datefieldToDate);
		
//		buttonSave=new Button(I18N.CONSTANTS.save());
//		buttonSave.addListener(Events.Select, new Listener<BaseEvent>() {
//			@Override
//			public void handleEvent(BaseEvent be) {
//				eventBus.fireEvent(new CreateEvent());
//			}
//		});
//		formPanel.addButton(buttonSave);
//		
//		buttonCancel = new Button(I18N.CONSTANTS.cancel());
//		buttonCancel.addListener(Events.Select, new Listener<BaseEvent>() {
//			@Override
//			public void handleEvent(BaseEvent be) {
//				eventBus.fireEvent(new CancelCreateEvent());
//			}
//		});
//		formPanel.addButton(buttonCancel);

	}

	@Override
	public void initialize() {

	}

	@Override
	public AsyncMonitor getLoadingMonitor() {
		return null;
	}

	@Override
	public void setValue(LockedPeriodDTO value) {
		if (value != null) {
			textfieldName.setValue(value.getName());
			checkboxEnabled.setValue(value.isEnabled());
			datefieldFromDate.setValue(value.getFromDate().atMidnightInMyTimezone());
			datefieldToDate.setValue(value.getToDate().atMidnightInMyTimezone());
		}
	}

	@Override
	public LockedPeriodDTO getValue() {
		LockedPeriodDTO newLockedPeriod =  new LockedPeriodDTO();
		newLockedPeriod.setName(textfieldName.getValue());
		newLockedPeriod.setEnabled(checkboxEnabled.getValue());
		newLockedPeriod.setFromDate(datefieldFromDate.getValue());
		newLockedPeriod.setToDate(datefieldToDate.getValue());
		if (radioActivity.getValue() && comboboxactivities.getValue() != null) {
			newLockedPeriod.setParent(comboboxactivities.getValue());
		}
		if (radioProject.getValue() && comboboxProjects.getValue() != null) {
			newLockedPeriod.setParent(comboboxProjects.getValue());
		}
		if (radioDatabase.getValue()) {
			newLockedPeriod.setParent(userDatabase);
		}
		
		return newLockedPeriod;
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public HandlerRegistration addCreateHandler(
			org.sigmah.client.mvp.CanCreate.CreateHandler handler) {
		return eventBus.addHandler(CreateEvent.TYPE, handler);
	}

	@Override
	public void create(LockedPeriodDTO item) {
	}

	@Override
	public void setCreateEnabled(boolean createEnabled) {
	}

	@Override
	public HandlerRegistration addCancelCreateHandler(
			org.sigmah.client.mvp.CanCreate.CancelCreateHandler handler) {
		return eventBus.addHandler(CancelCreateEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addStartCreateHandler(
			org.sigmah.client.mvp.CanCreate.StartCreateHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startCreate() {
		this.show();
	}

	private void setStartState() {
		textfieldName.setValue(null);
		datefieldFromDate.setValue(null);
		datefieldToDate.setValue(null);
		checkboxEnabled.setValue(true);
		radioActivity.setValue(false);
		radioProject.setValue(false);
		radioDatabase.setValue(true);
	}

	@Override
	public void cancelCreate() {
		setStartState();
		this.hide();
	}

	@Override
	public void update(LockedPeriodDTO item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelUpdate(LockedPeriodDTO item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelUpdateAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUpdateEnabled(boolean updateEnabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerRegistration addUpdateHandler(
			org.sigmah.client.mvp.CanUpdate.UpdateHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addCancelUpdateHandler(
			org.sigmah.client.mvp.CanUpdate.CancelUpdateHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addRequestUpdateHandler(
			org.sigmah.client.mvp.CanUpdate.RequestUpdateHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AsyncMonitor getCreatingMonitor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AsyncMonitor getUpdatingMonitor() {
		// TODO Auto-generated method stub
		return null;
	}
}
