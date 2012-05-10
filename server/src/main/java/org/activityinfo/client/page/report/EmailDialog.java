package org.activityinfo.client.page.report;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.report.EmailDialog.Callback;
import org.activityinfo.client.widget.MappingComboBox;
import org.activityinfo.shared.command.UpdateReportSubscription;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ReportMetadataDTO;
import org.activityinfo.shared.report.model.EmailDelivery;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Allows users with design priviledges to share with other users who have 
 * access to database. 
 * 
 */
public class EmailDialog extends Dialog {

	private Dispatcher dispatcher;
	
	private Radio none;
	private Radio weekly;
	private Radio monthly;
	private RadioGroup emailFrequency;
	private MappingComboBox<Integer> dayOfWeek;
	private MappingComboBox<Integer> dayOfMonth;

	private ReportMetadataDTO reportMetadata;

	private Callback callback;
	
	
	public interface Callback {
		void onUpdated();
	}
	
	public EmailDialog(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		setHeading("Email options");
		setWidth(450);
		setHeight(250);
						
		createLayout();
		
		setButtons(OKCANCEL);
	}

	public void createLayout() {
		FormPanel form = new FormPanel();
		form.setHeaderVisible(false);
		form.setLabelWidth(100);
		setLayout(new FitLayout());
		add(form);
		
		none = new Radio();
		none.setBoxLabel(I18N.CONSTANTS.none());
		weekly = new Radio();
		weekly.setBoxLabel(I18N.CONSTANTS.weekly());
		weekly.setValue(true);
		monthly = new Radio();
		monthly.setBoxLabel(I18N.CONSTANTS.monthly());

		emailFrequency = new RadioGroup();
		emailFrequency.setFieldLabel(I18N.CONSTANTS.emailFrequency());
		emailFrequency.setOrientation(Orientation.VERTICAL);
		emailFrequency.add(none);
		emailFrequency.add(weekly);
		emailFrequency.add(monthly);

		form.add(emailFrequency);

		dayOfWeek = new MappingComboBox<Integer>();
		dayOfWeek.setAllowBlank(false);
		dayOfWeek.setEditable(false);
		dayOfWeek.setFieldLabel(I18N.CONSTANTS.dayOfWeek());
		
		String[] weekDays = LocaleInfo.getCurrentLocale().getDateTimeConstants().weekdays();
		for(int i=0;i!=weekDays.length;++i) {
			dayOfWeek.add(i+1, weekDays[i]);
		}
		form.add(dayOfWeek);

		dayOfMonth = new MappingComboBox<Integer>();
		dayOfMonth.setEditable(false);
		dayOfMonth.hide();
		dayOfMonth.setFieldLabel(I18N.CONSTANTS.dayOfMonth());
		for (int i = 1; i <= 31; i++) {
			dayOfMonth.add(i, String.valueOf(i));
		}
		form.add(dayOfMonth);

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

	}

	public void show(ReportMetadataDTO report, Callback callback) {
		super.show();
		this.reportMetadata = report;
		this.callback = callback;
		
		if(report.getEmailDelivery().equals(EmailDelivery.WEEKLY)){
			dayOfWeek.setMappedValue(report.getDay());
			weekly.setValue(true);
			monthly.setValue(false);
			
			showWeek();
		} else if(report.getEmailDelivery().equals(EmailDelivery.MONTHLY)){
			dayOfMonth.setMappedValue(report.getDay());
			weekly.setValue(false);
			monthly.setValue(true);
			
			showMonth();
		} else {
			dayOfWeek.hide();
			dayOfMonth.hide();
			none.setValue(true);
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

	@Override
	protected void onButtonPressed(Button button) {
		if(button.getItemId().equals(OK)) {
			final UpdateReportSubscription update = new UpdateReportSubscription();
			update.setReportId(reportMetadata.getId());
			if(weekly.getValue()) {
				update.setEmailDelivery(EmailDelivery.WEEKLY);
				update.setEmailDay(dayOfWeek.getMappedValue());
			} else if(monthly.getValue()) {
				update.setEmailDelivery(EmailDelivery.MONTHLY);
				update.setEmailDay(dayOfMonth.getMappedValue());
			} else {
				update.setEmailDelivery(EmailDelivery.NONE);
			}
			dispatcher.execute(update, new MaskingAsyncMonitor(this, I18N.CONSTANTS.saving()), new AsyncCallback<VoidResult>() {
	
				@Override
				public void onFailure(Throwable caught) {
					
				}
	
				@Override
				public void onSuccess(VoidResult result) {
					hide();
					reportMetadata.setEmailDelivery(update.getEmailDelivery());
					reportMetadata.setDay(update.getEmailDay());
					callback.onUpdated();
				}
			});
		} else if(button.getItemId().equals(CANCEL)) {
			hide();
		}
	}
	
}
