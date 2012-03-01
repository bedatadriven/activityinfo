package org.sigmah.client.page.report.template;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.Report;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class PivotTableTemplate extends ReportTemplate {

	public PivotTableTemplate(Dispatcher dispatcher) {
		super(dispatcher);
		setName(I18N.CONSTANTS.pivotTables());
		setDescription(I18N.CONSTANTS.pivotTableDescription());
		setImagePath("pivot.png");
	}

	@Override
	public void create(AsyncCallback<Integer> callback) {
		Report report = new Report();
		report.addElement(new PivotTableReportElement());
		
		save(report, callback);
	}
	
	

}
