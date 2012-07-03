package org.activityinfo.client.page.report.template;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.report.model.Report;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public abstract class ReportTemplate extends BaseModelData {
	
	protected final Dispatcher dispatcher;
	
	@Inject
	public ReportTemplate(Dispatcher dispatcher) {
		super();
		this.dispatcher = dispatcher;
	}

	public void setName(String name) {
		set("name", name);
	}
	
	public void setDescription(String description) {
		set("description", description);
	}
	
	public void setImagePath(String path) {
		set("path", path);
	}
	
	public abstract void createReport(AsyncCallback<Report> report);

	
}
