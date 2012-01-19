package org.sigmah.client.page.common;

import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.widget.MappingComboBox;
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

	public SubscribeForm() {

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
		dayOfWeek.setFieldLabel(I18N.CONSTANTS.dayOfWeek());
		
		String[] weekDays = LocaleInfo.getCurrentLocale().getDateTimeConstants().weekdays();
		for(int i=0;i!=weekDays.length;++i) {
			dayOfWeek.add(i+1, weekDays[i]);
		}
		add(dayOfWeek);

		dayOfMonth = new MappingComboBox();
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
					dayOfMonth.hide();
					dayOfMonth.setAllowBlank(true);
					dayOfWeek.setAllowBlank(false);
					dayOfWeek.show();

				} else if (monthly.getValue()) {
					dayOfWeek.hide();
					dayOfWeek.setAllowBlank(true);
					dayOfMonth.setAllowBlank(false);
					dayOfMonth.show();
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
				store.remove(subscribers.getSelection().get(0));
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

	public List<ReportSubscriber> getEmailList() {

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

}
