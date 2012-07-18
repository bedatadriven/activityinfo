package org.activityinfo.client.page.report.template;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.ReportElement;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class ReportElementTemplate extends ReportTemplate {

	public ReportElementTemplate(Dispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void createReport(final AsyncCallback<Report> callback) {
		createElement(new AsyncCallback<ReportElement>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(ReportElement result) {
				Report report = new Report();
				report.addElement(result);
				callback.onSuccess(report);
			}
		});
	}
	
	public abstract void createElement(AsyncCallback<ReportElement> callback);
	
}
