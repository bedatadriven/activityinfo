package org.sigmah.client.page.report.template;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.report.model.DateDimension;
import org.sigmah.shared.report.model.DateUnit;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
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
		PivotTableReportElement table = new PivotTableReportElement();
		table.addColDimension(new DateDimension(DateUnit.YEAR));
		table.addColDimension(new DateDimension(DateUnit.MONTH));
		table.addRowDimension(new Dimension(DimensionType.Partner));
		
		Report report = new Report();
		report.addElement(table);
		
		save(report, callback);
	}
	
	

}
