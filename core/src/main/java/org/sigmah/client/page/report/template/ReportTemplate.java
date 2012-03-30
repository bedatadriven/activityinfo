package org.sigmah.client.page.report.template;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.report.json.ReportSerializer;
import org.sigmah.shared.command.CreateReport;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.report.model.Report;

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
	
	public abstract void create(AsyncCallback<Integer> callback);
	

	protected final void save(Report report, final AsyncCallback<Integer> callback) {	
		

		dispatcher.execute(new CreateReport(report), null, new AsyncCallback<CreateResult>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(CreateResult result) {
				callback.onSuccess(result.getNewId());
			}
		});
	}

}
