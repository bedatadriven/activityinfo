package org.sigmah.client.page.report.editor;

import org.sigmah.shared.report.model.ReportElement;

public interface AbstractEditor {

	ReportElement getReportElement();
	
	Object getWidget();
	
	void bindReportElement(ReportElement element);	
}
