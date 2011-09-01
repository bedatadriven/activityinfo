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
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UpdateStream extends AiPortlet {
	private SchemaDTO schema;

	public UpdateStream(Dispatcher service) {
		super(service, "Recently updated sites");
		
		initializeComponent();
		
		getSchema();
	}

	private void initializeComponent() {
		setAutoHeight(true);
		setAutoWidth(true);
		setLayout(new FitLayout());
	}
	
	private void getSchema() {
		service.execute(new GetSchema(), loadingMonitor, new AsyncCallback<SchemaDTO>() {
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
		service.execute(new GetSitesWithoutCoordinates(), loadingMonitor, new AsyncCallback<SitesWithoutLocationsResult>() { 
			@Override
			public void onFailure(Throwable caught) {
				//TODO: handle failure
			}
			@Override
			public void onSuccess(SitesWithoutLocationsResult result) {
				for (SiteDTO site : result.getData()) {
					add(new Update(site, schema.getActivityById(site.getActivityId())), new RowData(-1,-1,new Margins(5)));
				}
				layout(true);
			}
		});
	}
}

