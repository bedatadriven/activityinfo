package org.sigmah.client.page.common;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.widget.MappingComboBox;
import org.sigmah.server.database.hibernate.dao.ReportDefinitionDAO;
import org.sigmah.shared.dto.ReportDefinitionDTO;
import org.sigmah.shared.report.model.ReportFrequency;
import org.sigmah.shared.report.model.ReportSubscriber;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.i18n.client.LocaleInfo;

public class SubscribeForm extends FormPanel {

	private ListStore<ReportSubscriber> store;
	private TextField<String> title;
	private Radio weekly;
	private Radio monthly;
	private RadioGroup emailFrequency;
	private MappingComboBox dayOfWeek;
	private MappingComboBox dayOfMonth;
	private ListField<ReportSubscriber> subscribers;
	private TextField<String> newEmail;
	private Button addEmail;
	private Button removeEmail;
	
	private List<ReportSubscriber> unsubscribers;

	public SubscribeForm() {
		unsubscribers = new ArrayList<ReportSubscriber>();
		createLayout();
	}

	public void createLayout() {

		setLabelWidth(110);

		title = new TextField<String>();
		title.setWidth("300px");
		title.setFieldLabel(I18N.CONSTANTS.title());
		title.setAllowBlank(false);
		title.setName("title");
		add(title);

		weekly = new Radio();
		weekly.setBoxLabel(I18N.CONSTANTS.weekly());
		weekly.setValue(true);
		monthly = new Radio();
		monthly.setBoxLabel(I18N.CONSTANTS.monthly());

		emailFrequency = new RadioGroup();
		emailFrequency.setFieldLabel(I18N.CONSTANTS.emailFrequency());
		emailFrequency.setOrientation(Orientation.VERTICAL);
		emailFrequency.add(weekly);
		emailFrequency.add(monthly);

		add(emailFrequency);

		dayOfWeek = new MappingComboBox();
		dayOfWeek.setAllowBlank(false);
		dayOfWeek.setEditable(false);
		dayOfWeek.setFieldLabel(I18N.CONSTANTS.dayOfWeek());
		
		String[] weekDays = LocaleInfo.getCurrentLocale().getDateTimeConstants().weekdays();
		for(int i=0;i!=weekDays.length;++i) {
			dayOfWeek.add(i+1, weekDays[i]);
		}
		add(dayOfWeek);

		dayOfMonth = new MappingComboBox();
		dayOfMonth.setEditable(false);
		dayOfMonth.hide();
		dayOfMonth.setFieldLabel(I18N.CONSTANTS.dayOfMonth());
		for (int i = 1; i <= 31; i++) {
			dayOfMonth.add(i, String.valueOf(i));
		}
		add(dayOfMonth);

		emailFrequency.addListener(Events.Change, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				if (weekly.getValue()) {
					showWeek();

				} else if (monthly.getValue()) {
					showMonth();
				}
			}
		});

		store = new ListStore<ReportSubscriber>();

		subscribers = new ListField<ReportSubscriber>();
		subscribers.setFieldLabel(I18N.CONSTANTS.subscribers());
		subscribers.setDisplayField("email");
		subscribers.setStore(store);
		subscribers
				.addSelectionChangedListener(new SelectionChangedListener<ReportSubscriber>() {

					@Override
					public void selectionChanged(
							SelectionChangedEvent<ReportSubscriber> se) {
						removeEmail.setEnabled(true);
					}
				});

		add(subscribers);

		newEmailPanel();
	}

	public void newEmailPanel() {

		HorizontalPanel hp = new HorizontalPanel();

		HorizontalPanel pf = new HorizontalPanel();
		pf.setWidth(115);
		hp.add(pf);

		newEmail = new TextField<String>();
		newEmail.setEmptyText(I18N.CONSTANTS.enterNewEmail());
		hp.add(newEmail);

		addEmail = new Button();
		addEmail.setText(I18N.CONSTANTS.add());
		hp.add(addEmail);

		addEmail.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (newEmail.getValue() != null) {
					store.add(new ReportSubscriber(newEmail.getValue()));
					newEmail.setValue(null);
				}
			}
		});

		HorizontalPanel pm = new HorizontalPanel();
		pm.setWidth(50);
		hp.add(pm);

		removeEmail = new Button();
		removeEmail.setText(I18N.CONSTANTS.remove());
		removeEmail.setEnabled(false);
		hp.add(removeEmail);

		removeEmail.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				ReportSubscriber removeSub = subscribers.getSelection().get(0);
				removeSub.setSubscribed(false);
				unsubscribers.add(removeSub);
				store.remove(removeSub);
				removeEmail.setEnabled(false);
			}
		});

		add(hp);
	}

	@Override
	public String getTitle() {
		return title.getValue();
	}

	public void setReadOnlyTitle(String text){
		title.setValue(text);
		title.setReadOnly(true);
	}
	
	public ReportFrequency getReportFrequency() {
		if (monthly.getValue()) {
			return ReportFrequency.Monthly;
		} else {
			return ReportFrequency.Weekly;
		}
	}

	public int getDay() {
		if (monthly.getValue()) {
			return (Integer) ((ModelData) dayOfMonth.getSelection().get(0)).get("value");
		} else {
			return (Integer) ((ModelData) dayOfWeek.getSelection().get(0)).get("value");
		}
	}
	
	public void setFrequency(ReportFrequency freq, int day){
		if(freq.equals(ReportFrequency.Weekly)){
			ModelData selectedDay = (ModelData) dayOfWeek.getStore().getModels().get((day - 1));
			dayOfWeek.setMappedValue(selectedDay.get("value"));
			weekly.setValue(true);
			monthly.setValue(false);
			
			showWeek();
		}
		else if(freq.equals(ReportFrequency.Monthly)){
			ModelData selectedDay = (ModelData) dayOfMonth.getStore().getModels().get(day);
			dayOfMonth.setMappedValue(selectedDay.get("value"));
			weekly.setValue(false);
			monthly.setValue(true);
			
			showMonth();
		}
	}

	public List<ReportSubscriber> getEmailList() {
		if(unsubscribers.size() > 0 ){
			for(ReportSubscriber unSub : unsubscribers){
				store.add(unSub);
			}
		}
		return store.getModels();

	}
	
	public void setEmailList(List<String> emails){
		for(String email : emails){
			store.add(new ReportSubscriber(email));
		}
	}

	public boolean validListField() {
		if (store.getModels().size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public void showWeek(){
		dayOfMonth.hide();
		dayOfMonth.setAllowBlank(true);
		dayOfWeek.setAllowBlank(false);
		dayOfWeek.show();
	}
	
	public void showMonth(){
		dayOfWeek.hide();
		dayOfWeek.setAllowBlank(true);
		dayOfMonth.setAllowBlank(false);
		dayOfMonth.show();
	}
	
	public void updateForm(ReportDefinitionDTO report) {
		setReadOnlyTitle(report.getTitle());
		setEmailList(report.getSubscribers());
		if(report.getFrequency() != null && report.getDay() != null ){
			setFrequency(report.getFrequency(), report.getDay());
		}
	}
}
