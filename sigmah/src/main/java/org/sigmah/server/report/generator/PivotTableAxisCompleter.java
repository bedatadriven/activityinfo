package org.sigmah.server.report.generator;

import org.sigmah.shared.report.content.PivotTableData;

public interface PivotTableAxisCompleter {

	
	void complete(PivotTableData.Axis axis);
	
}
