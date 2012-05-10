package org.activityinfo.client.page.report.template;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.report.json.ReportSerializer;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.Report;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChartTemplate extends ReportTemplate {

	
	public ChartTemplate(Dispatcher dispatcher) {
		super(dispatcher);
		
		setName(I18N.CONSTANTS.charts());
		setDescription(I18N.CONSTANTS.chartsDescription());
		setImagePath("time.png");
	}
	
	@Override
	public void create(final AsyncCallback<Integer> callback) {

		PivotChartReportElement chart = new PivotChartReportElement();
		chart.setCategoryDimension(new DateDimension(DateUnit.YEAR));
		
		Report report = new Report();
		report.addElement(chart);
		
		save(report, callback);
	}
}
