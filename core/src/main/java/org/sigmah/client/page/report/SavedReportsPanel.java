package org.sigmah.client.page.report;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.GetReports;
import org.sigmah.shared.command.result.ReportsResult;
import org.sigmah.shared.dto.ReportMetadataDTO;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SavedReportsPanel extends ContentPanel implements ActionListener {
	
	public SavedReportsPanel(final EventBus eventBus, Dispatcher dispatcher) {
		setHeading("Saved Reports");
		
		ActionToolBar toolBar = new ActionToolBar(this);
		toolBar.addButton(UIActions.PRINT, "Open", IconImageBundle.ICONS.pdf());
		toolBar.addDeleteButton();
		setTopComponent(toolBar);
		
		ListLoader<ReportsResult> loader = new BaseListLoader<ReportsResult>(new ReportsProxy(dispatcher));
		ListStore<ReportMetadataDTO> store = new ListStore<ReportMetadataDTO>(loader);
		
		ListView<ReportMetadataDTO> listView = new ListView<ReportMetadataDTO>(store);
		listView.setSimpleTemplate("{title}");
		listView.addListener(Events.DoubleClick, new Listener<ListViewEvent<ReportMetadataDTO>>() {

			@Override
			public void handleEvent(ListViewEvent<ReportMetadataDTO> be) {
				
				eventBus.fireEvent(new NavigationEvent(
						NavigationHandler.NavigationRequested,
						new ReportDesignPageState(be.getModel().getId())));
			}
		});
		
		setLayout(new FitLayout());
		add(listView);
		
		loader.load();
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

	@Override
	public void onUIAction(String actionId) {
		// TODO Auto-generated method stub
		
	}
}
