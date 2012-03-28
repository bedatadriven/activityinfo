package org.sigmah.client.page.report;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.ReportVisibilityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.report.model.Report;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ShareReportDialog extends Dialog {
	
	private Dispatcher dispatcher;
	private ListStore<ReportVisibilityDTO> gridStore;
	private CheckColumnConfig visibleColumn;
	private CheckColumnConfig dashboardColumn;

	public ShareReportDialog(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		
		setWidth(450);
		setHeight(350);
		//add(new Label("You can share your report with other users of databases for which you have design permissions."));
		
		gridStore = new ListStore<ReportVisibilityDTO>();
		
		Grid<ReportVisibilityDTO> grid = new Grid<ReportVisibilityDTO>(gridStore, createColumnModel());
		grid.addPlugin(visibleColumn);
		grid.addPlugin(dashboardColumn);
		add(grid);
		
		setLayout(new FitLayout());
		
		
	}

	private ColumnModel createColumnModel() {
		ColumnConfig name = new ColumnConfig("databaseName", I18N.CONSTANTS.database(), 150);
		
        visibleColumn = new CheckColumnConfig("visible", "Visible?", 75);
        visibleColumn.setDataIndex("visible");

        dashboardColumn = new CheckColumnConfig("defaultDashboard", "Dashboard?", 75);
        dashboardColumn.setDataIndex("defaultDashboard");
        
        
        ColumnModel columnModel = new ColumnModel(Arrays.asList(name, visibleColumn, dashboardColumn));
		return columnModel;
	}
	
	public void show(final Report report) {
		super.show();
		
		// first fetch the list of databases from which this report draws
		dispatcher.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onSuccess(SchemaDTO schema) {
				Set<Integer> indicators = report.getIndicators();
				for(UserDatabaseDTO db : schema.getDatabases()) {
					if(hasAny(db, indicators)) {
						ReportVisibilityDTO visibility = new ReportVisibilityDTO();
						visibility.setDatabaseId(db.getId());
						visibility.setDatabaseName(db.getName());
						gridStore.add(visibility);
					}
				}
				
			}
			
		});
		
		
	}

	protected void createForm(List<UserDatabaseDTO> databases) {
		// TODO Auto-generated method stub
		
	}

	protected boolean hasAny(UserDatabaseDTO db, Set<Integer> indicators) {
		for(ActivityDTO activity : db.getActivities()) {
			for(IndicatorDTO indicator :activity.getIndicators()) {
				if(indicators.contains(indicator.getId())) {
					return true;
				}
			}
		}
		return false;
	}

}
