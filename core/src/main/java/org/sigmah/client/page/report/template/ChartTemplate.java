package org.sigmah.client.page.report.template;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.report.json.ReportSerializer;
import org.sigmah.shared.report.model.DateDimension;
import org.sigmah.shared.report.model.DateUnit;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.Report;

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
