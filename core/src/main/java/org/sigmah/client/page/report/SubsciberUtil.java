package org.sigmah.client.page.report;

import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.report.json.ReportSerializer;
import org.sigmah.shared.command.CreateReportDef;
import org.sigmah.shared.command.CreateSubscribe;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;
import org.sigmah.shared.report.model.ReportFrequency;
import org.sigmah.shared.report.model.ReportSubscriber;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SubsciberUtil {

	final Dispatcher service;
	final ReportSerializer reportSerializer;
	
    private SubscribeForm form;
    private FormDialogImpl dialog;
    
	public interface Callback{
		void onFailure();
		void onSuccess();
	}
	
	public SubsciberUtil(Dispatcher service, ReportSerializer reportSerializer){
		this.service = service;
		this.reportSerializer = reportSerializer;
	}
	
	public void showForm(final ReportElement element, final Callback callback){
		form = new SubscribeForm();
    	
		dialog = new FormDialogImpl(form);
		dialog.setWidth(450);
		dialog.setHeight(400);
		dialog.setHeading(I18N.CONSTANTS.SubscribeTitle());
		
		dialog.show(new FormDialogCallback() {
			@Override
			public void onValidated() {
				if(form.validListField()){
					createReport(element, callback);	
				} else{
					MessageBox.alert(I18N.CONSTANTS.error(), I18N.MESSAGES.noEmailAddress(), null);
				}
            }
		});
	}
	
	  public void createReport(ReportElement element, final Callback callback){
	    	
	    	final Report report = new Report();
	    	report.addElement(element);
	    	report.setDay(form.getDay());
	    	report.setTitle(form.getTitle());
	    	report.setFrequency(form.getReportFrequency());
			
			service.execute(new CreateReportDef(reportSerializer.serialize(report)), null, new AsyncCallback<CreateResult>() {
	            public void onFailure(Throwable caught) {
	            	dialog.onServerError();
	            }

	            public void onSuccess(CreateResult result) {
	            	subscribeEmail(result.getNewId(), callback);
	            }
	        });
	    	
	    }
	  
	 public void subscribeEmail(int templateId, final Callback callback){
	    subscribeEmail(templateId, form.getEmailList(), form.getDay(), form.getReportFrequency(), callback);
	 }
	 
	 private void subscribeEmail(int reportTemplateId,List<ReportSubscriber> emailsList,int day,ReportFrequency reportFrequency, final Callback callback) {

		CreateSubscribe sub = new CreateSubscribe();
		sub.setReportTemplateId(reportTemplateId);
		sub.setEmailsList(emailsList);
		sub.setDay(day);
		sub.setReportFrequency(reportFrequency);

		service.execute(sub, null, new AsyncCallback<VoidResult>() {
			public void onFailure(Throwable caught) {
				dialog.onServerError();
				callback.onFailure();
			}

			public void onSuccess(VoidResult result) {
				dialog.hide();
				callback.onSuccess();
			}
		});
	}
}
