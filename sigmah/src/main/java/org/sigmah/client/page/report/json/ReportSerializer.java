package org.sigmah.client.page.report.json;

import org.sigmah.shared.report.model.Report;

public interface ReportSerializer {

	String serialize(Report element);

	Report deserialize(String json);
}
