package org.activityinfo.server.report.generator;

import java.util.Map;

import org.activityinfo.server.domain.User;
import org.activityinfo.shared.report.model.Filter;
import org.activityinfo.shared.report.model.ReportElement;

public interface ContentGenerator<T extends ReportElement> {
	

    void generate(User user, T element, Filter inheritedFilter, Map<String, Object> parameters);
	
}
