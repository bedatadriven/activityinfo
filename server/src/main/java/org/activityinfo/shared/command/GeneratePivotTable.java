package org.activityinfo.shared.command;

import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.model.PivotTableReportElement;

public class GeneratePivotTable implements Command<PivotContent> {
	private PivotTableReportElement model;
	
	public GeneratePivotTable() {
		
	}

	public GeneratePivotTable(PivotTableReportElement model) {
		super();
		this.model = model;
	}

	public PivotTableReportElement getModel() {
		return model;
	}

	public void setModel(PivotTableReportElement model) {
		this.model = model;
	}
}
