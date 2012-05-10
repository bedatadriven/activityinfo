package org.activityinfo.client.page.report.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface ReportResources extends ClientBundle {

	public static final ReportResources INSTANCE = GWT.create(ReportResources.class);
	
	interface Style extends CssResource {
		String bar();
		String page();
	}
	
	@Source("Report.css")
	Style style();

}
