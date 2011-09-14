package org.sigmah.client.page.dashboard;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.GetSitesWithoutCoordinates;
import org.sigmah.shared.command.result.SitesWithoutLocationsResult;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UpdateStream extends VerticalPanel {
	private SchemaDTO schema;
	private Dispatcher service;
	//private AsyncMonitor loadingMonitor = new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading());
	
	public UpdateStream(Dispatcher service) {
		this.service=service;
		initializeComponent();
		
		getSchema();
	}

	private void initializeComponent() {
	}
	
	private void getSchema() {
		service.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO: handle failure
				System.out.println();
			}
			@Override
			public void onSuccess(SchemaDTO result) {
				UpdateStream.this.schema=result;
				UpdateStream.this.getSites();
			}
		});
	}

	private void getSites() {
		GetSites getSites = new GetSites();
		getSites.setLimit(10);
		getSites.setSortInfo(new SortInfo("DateEdited", SortDir.DESC));
		service.execute(new GetSitesWithoutCoordinates(), null, new AsyncCallback<SitesWithoutLocationsResult>() { 
			@Override
			public void onFailure(Throwable caught) {
				//TODO: handle failure
			}
			@Override
			public void onSuccess(SitesWithoutLocationsResult result) {
				for (SiteDTO site : result.getData()) {
					add(new Update(site, schema.getActivityById(site.getActivityId())));
				}
			}
		});
	}
}

