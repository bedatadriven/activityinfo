package org.activityinfo.client.page.report.json;

import org.activityinfo.shared.report.model.Report;

import com.google.gwt.user.client.rpc.SerializationException;

public interface ReportSerializer {

	String serialize(Report report);

	Report deserialize(String json) ;
}
