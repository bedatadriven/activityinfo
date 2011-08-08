package org.sigmah.client.page.config;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.config.LockedPeriodsPresenter.AddLockedPeriodView;
import org.sigmah.shared.dto.LockedPeriodDTO;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;

public class AddLockedPeriodDialog extends Dialog implements
		AddLockedPeriodView {

	private FormPanel formPanel;
	private TextField<String> textfieldName;
	private DateField datefieldFromDate;
	private DateField datefieldToDate;
	private CheckBox checkboxEnabled;
	private LabelField labelParent;
	private LabelField labelTimePeriod;  // Shows the total time
	private Button buttonSave;
	private Button buttonCancel;
	
	private EventBus eventBus = new SimpleEventBus();
	
	public AddLockedPeriodDialog() {
		super();
		
		initializeComponent();
	}

	private void initializeComponent() {
		formPanel = new FormPanel();
		
		textfieldName = new TextField<String>();
		textfieldName.setFieldLabel(I18N.CONSTANTS.name());
		formPanel.add(textfieldName);
	
		checkboxEnabled = new CheckBox();
		checkboxEnabled.setFieldLabel("Enabled");
		formPanel.add(checkboxEnabled);
		
		datefieldFromDate = new DateField();
		datefieldFromDate.setFieldLabel(I18N.CONSTANTS.fromDate());
		formPanel.add(datefieldFromDate);
		
		datefieldToDate = new DateField();
		datefieldToDate.setFieldLabel(I18N.CONSTANTS.toDate());
		formPanel.add(datefieldToDate);
		
		buttonSave=new Button(I18N.CONSTANTS.save());
		buttonSave.addListener(Events.Select, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				eventBus.fireEvent(new CreateEvent());
			}
		});
		formPanel.addButton(buttonSave);
		
		buttonCancel = new Button(I18N.CONSTANTS.cancel());
		buttonCancel.addListener(Events.Select, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				eventBus.fireEvent(new CancelCreateEvent());
			}
		});

		add(formPanel);
	}

	@Override
	public void initialize() {

	}

	@Override
	public AsyncMonitor getAsyncMonitor() {
		return null;
	}

	@Override
	public void setValue(LockedPeriodDTO value) {
		if (value != null) {
			textfieldName.setValue(value.getName());
			checkboxEnabled.setValue(value.isEnabled());
			datefieldFromDate.setValue(value.getStartDate());
			datefieldToDate.setValue(value.getEndDate());
		}
	}

	@Override
	public LockedPeriodDTO getValue() {
		LockedPeriodDTO newLockedPeriod =  new LockedPeriodDTO();
		newLockedPeriod.setName(textfieldName.getValue());
		newLockedPeriod.setEnabled(checkboxEnabled.getValue());
		newLockedPeriod.setStartDate(datefieldFromDate.getValue());
		newLockedPeriod.setEndDate(datefieldToDate.getValue());
		
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

	@Override
	public void stopCreate() {
		this.hide();
	}

}
