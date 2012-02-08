package org.sigmah.client.page.report;

import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.GetReportTemplates;
import org.sigmah.shared.command.result.ReportTemplateResult;
import org.sigmah.shared.dto.ReportDefinitionDTO;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SavedReportsPanel extends ContentPanel implements ActionListener {
	
	public SavedReportsPanel(Dispatcher dispatcher) {
		setHeading("Saved Reports");
		
		ActionToolBar toolBar = new ActionToolBar(this);
		toolBar.addButton(UIActions.PRINT, "Open", IconImageBundle.ICONS.pdf());
		toolBar.addDeleteButton();
		setTopComponent(toolBar);
		
		ListLoader<ReportTemplateResult> loader = new BaseListLoader<ReportTemplateResult>(new ReportsProxy(dispatcher));
		ListStore<ReportDefinitionDTO> store = new ListStore<ReportDefinitionDTO>(loader);
		
		ListView<ReportDefinitionDTO> listView = new ListView<ReportDefinitionDTO>(store);
		listView.setSimpleTemplate("{title}");
		
		setLayout(new FitLayout());
		add(listView);
		
		loader.load();
	}

	private class ReportsProxy extends RpcProxy<ReportTemplateResult> {

		private final Dispatcher dispatcher;
		
		public ReportsProxy(Dispatcher dispatcher) {
			super();
			this.dispatcher = dispatcher;
		}

		@Override
		protected void load(Object loadConfig,
				final AsyncCallback<ReportTemplateResult> callback) {
			
			dispatcher.execute(new GetReportTemplates(), null, new AsyncCallback<ReportTemplateResult>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccess(ReportTemplateResult result) {
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
