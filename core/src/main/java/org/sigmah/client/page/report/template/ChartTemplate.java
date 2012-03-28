package org.sigmah.client.page.report.template;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.report.json.ReportSerializer;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.Report;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChartTemplate extends ReportTemplate {

	private ReportSerializer reportSerializer;
	
	public ChartTemplate(Dispatcher dispatcher, ReportSerializer reportSerializer) {
		super(dispatcher, reportSerializer);
		this.reportSerializer = reportSerializer;
		
		setName(I18N.CONSTANTS.charts());
		setDescription(I18N.CONSTANTS.chartsDescription());
		setImagePath("time.png");
	}
	
	@Override
	public void create(final AsyncCallback<Integer> callback) {
	
		Report report = new Report();
		report.addElement(new PivotChartReportElement());
		
		save(report, callback);
	}
}
