package org.activityinfo.client.page.report.template;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.report.json.ReportSerializer;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.activityinfo.shared.report.model.Report;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class CompositeTemplate extends ReportTemplate {


	public CompositeTemplate(Dispatcher dispatcher) {
		super(dispatcher);

		setName(I18N.CONSTANTS.customReport());
		setDescription(I18N.CONSTANTS.customReportDescription());
		setImagePath("time.png");
	}

	@Override
	public void createReport(AsyncCallback<Report> callback) {
		callback.onSuccess(new Report());
	}

}
