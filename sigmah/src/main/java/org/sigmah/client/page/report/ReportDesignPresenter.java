package org.sigmah.client.page.report;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.Got;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.GetReport;
import org.sigmah.shared.command.GetUserReports;
import org.sigmah.shared.command.RenderReportHtml;
import org.sigmah.shared.command.result.HtmlResult;
import org.sigmah.shared.command.result.ReportTemplateResult;
import org.sigmah.shared.dto.ReportDefinitionDTO;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

public class ReportDesignPresenter implements ActionListener, Page {
	public static final PageId PAGE_ID = new PageId("reportdesign");

	private final EventBus eventBus;
	private final Dispatcher service;
	private final View view;
	private final ReportElementEditor elementEditor;

	@ImplementedBy(ReportDesignPage.class)
	public interface View {
		void init(ReportDesignPresenter presenter);

		void setReportElement(ReportDefinitionDTO dto);

		void setPreviewHtml(String html, String heading);

		AsyncMonitor getLoadingMonitor();

		ListStore<ModelData> getReportElements();

		void setPreview();

		void setReport(ReportDefinitionDTO dto);
		
		void addToCenterPanel(Widget w, String title);
	}

	@Inject
	public ReportDesignPresenter(EventBus eventBus, Dispatcher service,
			View view, ReportElementEditor elementEditor) {
		super();
		this.eventBus = eventBus;
		this.service = service;
		this.view = view;
		this.view.init(this);
		this.elementEditor = elementEditor;

	}

	public void go(int reportId) {
		loadReport(reportId);
	}

	@Override
	public void onUIAction(String actionId) {
		if (UIActions.ADDCHART.equals(actionId)) {
			addChart();
		} else if(UIActions.ADDMAP.equals(actionId)){
			addMap();
		}
		else if(UIActions.ADDTABLE.equals(actionId)){
			addTable();
		}
	}

	private void loadReport(int reportId) {

		service.execute(new GetReport(reportId), null,
				new AsyncCallback<ReportTemplateResult>() {
					@Override
					public void onFailure(Throwable caught) {
						// callback.onFailure(caught);
					}

					@Override
					public void onSuccess(ReportTemplateResult result) {
//						view.getReportListStore().removeAll();
						for (ReportDefinitionDTO report : result.getData()) {
							view.setReportElement(report);
							view.setReport(report);
						}

					}
				});
	}

	public void generateReportPreview(final ReportDefinitionDTO selectedReport) {
		RenderReportHtml command = new RenderReportHtml(selectedReport.getId(),
				null);
		service.execute(command, view.getLoadingMonitor(),
				new Got<HtmlResult>() {
					@Override
					public void got(HtmlResult result) {
						view.setPreviewHtml(result.getHtml(),
						I18N.CONSTANTS.reportPreview() + " : " +selectedReport.getTitle());
					}
				});
	}

	public void createEditor(ReportElement e){
		Widget w = (Widget)elementEditor.createEditor(e);
		view.addToCenterPanel(w,"Report Element Editor");
	}
	
	public void addChart(){
		Widget w = (Widget) elementEditor.createChart();
		view.addToCenterPanel(w,"New Chart");
	}
	
	public void addTable(){
		Widget w = (Widget) elementEditor.createTable();
		view.addToCenterPanel(w,"New Table");
	}
	
	public void addMap(){
		Widget w = (Widget) elementEditor.createMap();
		view.addToCenterPanel(w,"New Map");
	}
	
	public void loadReportComponents(int reportId) {

	}

	@Override
	public void shutdown() {

	}

	@Override
	public PageId getPageId() {
		return PAGE_ID;
	}

	@Override
	public Object getWidget() {
		return view;
	}

	@Override
	public void requestToNavigateAway(PageState place,
			NavigationCallback callback) {
		callback.onDecided(true);
	}

	@Override
	public String beforeWindowCloses() {
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		return false;
	}

}
