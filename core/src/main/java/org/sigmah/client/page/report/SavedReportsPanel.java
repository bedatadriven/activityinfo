package org.sigmah.client.page.report;

import java.util.Arrays;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.GetReports;
import org.sigmah.shared.command.UpdateReportSubscription;
import org.sigmah.shared.command.result.ReportsResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ReportMetadataDTO;
import org.sigmah.shared.report.model.ReportFrequency;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SavedReportsPanel extends ContentPanel implements ActionListener {
	private Dispatcher dispatcher;
	private ListStore<ReportMetadataDTO> store;
	private EventBus eventBus;
	private EditorGrid<ReportMetadataDTO> grid;
	private ActionToolBar toolBar;

	public SavedReportsPanel(final EventBus eventBus, Dispatcher dispatcher) {
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		
		setHeading("Saved Reports");
		
		toolBar = new ActionToolBar(this);
		toolBar.addButton(UIActions.PRINT, "Open", IconImageBundle.ICONS.pdf());
		toolBar.addDeleteButton();
		toolBar.addButton("email", "Email options...", IconImageBundle.ICONS.email());
		setTopComponent(toolBar);
		
		ListLoader<ReportsResult> loader = new BaseListLoader<ReportsResult>(new ReportsProxy(dispatcher));
		store = new ListStore<ReportMetadataDTO>(loader);
		
		grid = new EditorGrid<ReportMetadataDTO>(store, createColumnModel());
		grid.setStripeRows(true);
		grid.setAutoExpandColumn("title");
		grid.setSelectionModel(new GridSelectionModel<ReportMetadataDTO>());
		grid.setClicksToEdit(ClicksToEdit.ONE);
		grid.addListener(Events.CellDoubleClick, new Listener<GridEvent<ReportMetadataDTO>>() {

			@Override
			public void handleEvent(GridEvent<ReportMetadataDTO> be) {
				open(be.getModel());
			}
		});
		grid.addListener(Events.CellClick, new Listener<GridEvent<ReportMetadataDTO>>() {

			@Override
			public void handleEvent(GridEvent<ReportMetadataDTO> event) {
				if(event.getColIndex() == 0) {
					onDashboardClicked(event.getModel());
				}
			}
		});
		grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ReportMetadataDTO>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<ReportMetadataDTO> se) {
				onSelectionChanged(se.getSelectedItem());
			}
		});
		
		setLayout(new FitLayout());
		add(grid);
		
		loader.load();
	}


	private void onSelectionChanged(ReportMetadataDTO selectedItem) {
		toolBar.setActionEnabled(UIActions.DELETE, selectedItem != null &&
				selectedItem.getAmOwner());
		toolBar.setActionEnabled(UIActions.PRINT, selectedItem != null);
		toolBar.setActionEnabled("email", selectedItem != null);
	}


	private ColumnModel createColumnModel() {
		ColumnConfig dashboard = new ColumnConfig("dashboard", "", 28);
		dashboard.setHeader(IconImageBundle.ICONS.star().getHTML());
		dashboard.setResizable(false);
		dashboard.setMenuDisabled(true);
		dashboard.setRenderer(new GridCellRenderer<ReportMetadataDTO>() {

			@Override
			public Object render(ReportMetadataDTO model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ReportMetadataDTO> store, Grid<ReportMetadataDTO> grid) {

				return "<div style='cursor:pointer'>" + (model.isDashboard() ? IconImageBundle.ICONS.star().getHTML() :
					IconImageBundle.ICONS.starWhite().getHTML()) + "</div>";
				
			}
		});

		ColumnConfig title = new ColumnConfig("title", I18N.CONSTANTS.title(), 150);
		ColumnConfig owner = new ColumnConfig("ownerName", I18N.CONSTANTS.ownerName(), 100);
		owner.setRenderer(new GridCellRenderer<ReportMetadataDTO>() {

			@Override
			public Object render(ReportMetadataDTO model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ReportMetadataDTO> store,
					Grid<ReportMetadataDTO> grid) {
				
				if(model.getAmOwner()) {
					return "Me";
				} else {
					return model.getOwnerName();
				}
			}
		});
		
		
		ColumnConfig email = new ColumnConfig("email", I18N.CONSTANTS.emailSubscription(), 100);
		email.setRenderer(new GridCellRenderer<ReportMetadataDTO>() {

			@Override
			public Object render(ReportMetadataDTO model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ReportMetadataDTO> store, Grid<ReportMetadataDTO> grid) {

				if(model.isSubscribed()) {
					switch(model.getFrequency()) {
					case Monthly:
						return I18N.CONSTANTS.monthly();
					case Weekly:
					default:
						return I18N.CONSTANTS.weekly();
					}
				} else {
					return "<i>" + I18N.CONSTANTS.none() + "</i>";
				}
			}
		});
		
		
		return new ColumnModel(Arrays.asList(dashboard, title, owner, email));
	}

	private class ReportsProxy extends RpcProxy<ReportsResult> {

		private final Dispatcher dispatcher;
		
		public ReportsProxy(Dispatcher dispatcher) {
			super();
			this.dispatcher = dispatcher;
		}

		@Override
		protected void load(Object loadConfig,
				final AsyncCallback<ReportsResult> callback) {
			
			dispatcher.execute(new GetReports(), null, new AsyncCallback<ReportsResult>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccess(ReportsResult result) {
					callback.onSuccess(result);
				}
			});
		}
	}

	private void onDashboardClicked(final ReportMetadataDTO model) {
		model.setDashboard( ! model.isDashboard() );
		store.update(model);
		
		UpdateReportSubscription update = new UpdateReportSubscription();
		update.setReportId(model.getId());
		update.setPinnedToDashboard(model.isDashboard());
		
		dispatcher.execute(update, null, new AsyncCallback<VoidResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(VoidResult result) {
				Info.display(I18N.CONSTANTS.saved(), 
					model.isDashboard() ? I18N.MESSAGES.addedToDashboard(model.getTitle()) :
						I18N.MESSAGES.removedFromDashboard(model.getTitle()));
			}
		});
		
	}

	@Override
	public void onUIAction(String actionId) {
		if(UIActions.PRINT.equals(actionId)) {
			open(grid.getSelectionModel().getSelectedItem());
		} else if("email".equals(actionId)) {
			EmailDialog dialog = new EmailDialog(dispatcher);
			dialog.show(grid.getSelectionModel().getSelectedItem());
		}
	}

	private void open(ReportMetadataDTO model) {
		eventBus.fireEvent(new NavigationEvent(
				NavigationHandler.NavigationRequested,
				new ReportDesignPageState(model.getId())));
	}
}
