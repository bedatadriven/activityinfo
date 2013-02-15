package org.activityinfo.client.page.report;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.GetReportModel;
import org.activityinfo.shared.command.GetReportVisibility;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.UpdateReportVisibility;
import org.activityinfo.shared.command.result.BatchResult;
import org.activityinfo.shared.command.result.ReportVisibilityResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.ReportDTO;
import org.activityinfo.shared.dto.ReportMetadataDTO;
import org.activityinfo.shared.dto.ReportVisibilityDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.report.model.Report;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ShareReportDialog extends Dialog {
	
	private final Dispatcher dispatcher;
	private final ListStore<ReportVisibilityDTO> gridStore;
	private CheckColumnConfig visibleColumn;
	private CheckColumnConfig dashboardColumn;
	private Report currentReport;
	private final Grid<ReportVisibilityDTO> grid;

	public ShareReportDialog(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		
		setHeading(I18N.CONSTANTS.shareReport());
		setWidth(450);
		setHeight(350);	
		setButtons(Dialog.OKCANCEL);
		
		gridStore = new ListStore<ReportVisibilityDTO>();
		grid = new Grid<ReportVisibilityDTO>(gridStore, createColumnModel());
		grid.addPlugin(visibleColumn);
		grid.addPlugin(dashboardColumn);
		add(grid);
		
		setLayout(new FitLayout());
		
		
	}

	private ColumnModel createColumnModel() {
			
		ColumnConfig icon = new ColumnConfig("icon", "", 26);
		icon.setSortable(false);
		icon.setResizable(false);
		icon.setMenuDisabled(true);
		icon.setRenderer(new GridCellRenderer<ReportVisibilityDTO>() {

			@Override
			public Object render(ReportVisibilityDTO model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ReportVisibilityDTO> store,
					Grid<ReportVisibilityDTO> grid) {
				return IconImageBundle.ICONS.group().getHTML();
			}
		});
		
		ColumnConfig name = new ColumnConfig("databaseName", I18N.CONSTANTS.group(), 150);
		name.setRenderer(new GridCellRenderer<ReportVisibilityDTO>() {

			@Override
			public Object render(ReportVisibilityDTO model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ReportVisibilityDTO> store, Grid<ReportVisibilityDTO> grid) {
				
				return 
					model.getDatabaseName() + " Users";
				
			}
		});
		
        visibleColumn = new CheckColumnConfig("visible", I18N.CONSTANTS.shared(), 75);
        visibleColumn.setDataIndex("visible");

        dashboardColumn = new CheckColumnConfig("defaultDashboard", I18N.CONSTANTS.defaultDashboard(), 75);
        dashboardColumn.setDataIndex("defaultDashboard");
        
        
        ColumnModel columnModel = new ColumnModel(Arrays.asList(icon, name, visibleColumn, dashboardColumn));
		return columnModel;
	}
	
	public void show(ReportMetadataDTO metadata) {
		super.show();

		BatchCommand batch = new BatchCommand();
		batch.add(new GetReportModel(metadata.getId()));
		batch.add(new GetSchema());
		batch.add(new GetReportVisibility(metadata.getId()));
		
		dispatcher.execute(batch, new MaskingAsyncMonitor(grid, I18N.CONSTANTS.loading()),
				new AsyncCallback<BatchResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onSuccess(BatchResult batch) {

						currentReport = ((ReportDTO) batch.getResult(0))
								.getReport();
				
				populateGrid((SchemaDTO)batch.getResult(1), 
							 (ReportVisibilityResult)batch.getResult(2));
			}
		});
	}
	
	public void show(final Report report) {
		super.show();
		
		this.currentReport = report;
		
		// we need to combine the databases which already have visiblity with those
		// that could potentially be added
		BatchCommand batch = new BatchCommand();
		batch.add(new GetSchema());
		batch.add(new GetReportVisibility(currentReport.getId()));
		
		dispatcher.execute(batch, new MaskingAsyncMonitor(grid, I18N.CONSTANTS.loading()),
				new AsyncCallback<BatchResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onSuccess(BatchResult batch) {
				populateGrid((SchemaDTO)batch.getResult(0), 
							 (ReportVisibilityResult)batch.getResult(1));
			}
		});
	}
	
	private void populateGrid(SchemaDTO schema, ReportVisibilityResult visibility) {
		gridStore.removeAll();
		Set<Integer> indicators = currentReport.getIndicators();
		Map<Integer, ReportVisibilityDTO> databases = Maps.newHashMap();
		
		for(ReportVisibilityDTO model : visibility.getList()) {
			databases.put(model.getDatabaseId(), model);
		}
		
		for(UserDatabaseDTO db : schema.getDatabases()) {
			if( hasAny(db, indicators)) {
				if(databases.containsKey(db.getId())) {
					gridStore.add(databases.get(db.getId()));
				} else {
					ReportVisibilityDTO model = new ReportVisibilityDTO();
					model.setDatabaseId(db.getId());
					model.setDatabaseName(db.getName());
					gridStore.add(model);
				}
			}
		}	
		
		if(gridStore.getCount() == 0) {
			MessageBox.alert(I18N.CONSTANTS.share(), "This report is still empty, so it can't yet be shared.", 
					new Listener<MessageBoxEvent>() {
				
				@Override
				public void handleEvent(MessageBoxEvent be) {
					hide();
				}
			});
		}
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

	@Override
	protected void onButtonPressed(Button button) {
		if(button.getItemId().equals(Dialog.OK)) {
			save();
		} else {
			hide();
		}
	}

	private void save() {
		List<ReportVisibilityDTO> toSave = Lists.newArrayList();
		for(ReportVisibilityDTO model : gridStore.getModels()) {
			if(gridStore.getRecord(model).isDirty()) {
				toSave.add(model);
			}
		}
		
		if(toSave.isEmpty()) {
			hide();
		} else {
			dispatcher.execute(new UpdateReportVisibility(currentReport.getId(), toSave), 
				new MaskingAsyncMonitor(grid, I18N.CONSTANTS.saving()),
				new AsyncCallback<VoidResult>() {

					@Override
					public void onFailure(Throwable caught) {
						// handled by monitor
					}

					@Override
					public void onSuccess(VoidResult result) {
						hide();
					}
				});
		}
	}

}
