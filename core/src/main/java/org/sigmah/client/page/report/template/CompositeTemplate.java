package org.sigmah.client.page.report.template;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.Report;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class CompositeTemplate extends ReportTemplate {

	public CompositeTemplate(Dispatcher dispatcher) {
		super(dispatcher);

		setName(I18N.CONSTANTS.charts());
		setDescription(I18N.CONSTANTS.chartsDescription());
		setImagePath("time.png");
	}

	@Override
	public void create(AsyncCallback<Integer> callback) {
		Report report = new Report();
		
		save(report, callback);
		
	}

}
