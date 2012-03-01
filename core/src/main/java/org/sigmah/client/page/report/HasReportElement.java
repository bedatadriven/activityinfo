package org.sigmah.client.page.report;

import org.sigmah.shared.report.model.ReportElement;

public interface HasReportElement<M extends ReportElement> {

	void bind(M model);
	
	M getModel();
			
}
