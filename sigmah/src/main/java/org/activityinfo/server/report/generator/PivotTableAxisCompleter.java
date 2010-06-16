package org.activityinfo.server.report.generator;

import org.activityinfo.shared.report.content.PivotTableData;

public interface PivotTableAxisCompleter {

	
	void complete(PivotTableData.Axis axis);
	
}
