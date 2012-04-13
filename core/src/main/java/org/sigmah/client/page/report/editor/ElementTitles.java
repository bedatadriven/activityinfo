package org.sigmah.client.page.report.editor;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.ReportElement;

public final class ElementTitles {

	private ElementTitles() {}
	
	public static String format(ReportElement model) {
		if(model.getTitle() != null) {
			return model.getTitle();
		} else if(model instanceof PivotChartReportElement) {
			return I18N.CONSTANTS.untitledChart();
		} else if(model instanceof PivotTableReportElement) {
			return I18N.CONSTANTS.untitledTable();
		} else if(model instanceof MapReportElement) {
			return I18N.CONSTANTS.untitledMap();
		} else {
			return "Untitled";
		}
	}
	
}
