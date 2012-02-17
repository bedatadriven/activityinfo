package org.sigmah.client.page.charts;

import org.sigmah.shared.report.model.ReportElement;

public interface ReportView <T extends ReportElement> {

	void show(T element);
	
}
