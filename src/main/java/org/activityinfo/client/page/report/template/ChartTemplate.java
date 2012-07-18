package org.activityinfo.client.page.report.template;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.ReportElement;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChartTemplate extends ReportElementTemplate {

	
	public ChartTemplate(Dispatcher dispatcher) {
		super(dispatcher);
		
		setName(I18N.CONSTANTS.charts());
		setDescription(I18N.CONSTANTS.chartsDescription());
		setImagePath("time.png");
	}
	
	@Override
	public void createElement(final AsyncCallback<ReportElement> callback) {

		PivotChartReportElement chart = new PivotChartReportElement();
		chart.setCategoryDimension(new DateDimension(DateUnit.YEAR));
		
		callback.onSuccess(chart);
	}
}
