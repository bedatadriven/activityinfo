package org.sigmah.client.page.report;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.Got;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.SubscribeForm;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.CreateSubscribe;
import org.sigmah.shared.command.GetReport;
import org.sigmah.shared.command.GetUserReports;
import org.sigmah.shared.command.RenderReportHtml;
import org.sigmah.shared.command.UpdateReport;
import org.sigmah.shared.command.result.HtmlResult;
import org.sigmah.shared.command.result.ReportTemplateResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ReportDefinitionDTO;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
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

	private SubscribeForm form;
	private FormDialogImpl dialog;

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

		ReportDefinitionDTO getReport();

		void addElement(ReportElement element, boolean edited);

		Button getSaveButton();
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
		} else if (UIActions.ADDMAP.equals(actionId)) {
			addMap();
		} else if (UIActions.ADDTABLE.equals(actionId)) {
			addTable();
		} else if (UIActions.SAVE.equals(actionId)) {
			updateReport(view.getReport().getId(), null, getReportElements());
		} else if (UIActions.SUBSCRIBE.equals(actionId)) {
			createSubscribeForm();
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
								I18N.CONSTANTS.reportPreview() + " : "
										+ selectedReport.getTitle());
					}
				});
	}

	public void createEditor(ReportElement e) {
		Widget w = (Widget) elementEditor.createEditor(e);
		view.addToCenterPanel(w, "Report Element Editor");
	}

	public void addChart() {
		addNewElement("New Chart", (Widget) elementEditor.createChart());	
	}

	public void addTable() {
		addNewElement("New Table", (Widget) elementEditor.createTable());
	}

	public void addMap() {
		addNewElement("New Map", (Widget) elementEditor.createMap());		
	}

	private void addNewElement(String heading, Widget w) {
		if(isSaved()){
			unEditElements();
			addElementToView(w, heading);
		}		
	}

	private boolean isSaved(){
		
		if (view.getSaveButton().isEnabled()) {
			MessageBox.alert(I18N.CONSTANTS.error(), "Please save current report element.", null);
			return false;
		}
		return true;
	}
	
	private void addElementToView(Widget w, String heading){

		view.addToCenterPanel(w, heading);
		view.addElement(elementEditor.retriveReportElement(), true);
		view.getSaveButton().setEnabled(true);
	}
	
	public void loadReportComponents(int reportId) {

	}

	public void updateReport(int id, String title, List<ReportElement> elements) {
		UpdateReport updateReport = new UpdateReport();
		updateReport.setId(id);
		updateReport.setTitle(title);
		updateReport.setElement(elements);

		service.execute(updateReport, null, new AsyncCallback<VoidResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// callback.onFailure(caught);
			}

			@Override
			public void onSuccess(VoidResult result) {
				loadReport(view.getReport().getId());
				view.getSaveButton().setEnabled(false);
				view.addToCenterPanel(null, I18N.CONSTANTS.reportPreview());
			}
		});
	}

	public List<ReportElement> getReportElements() {
		List<ReportElement> elements = new ArrayList<ReportElement>();
		List<ModelData> store = view.getReportElements().getModels();
		for (int i = 0; i < store.size(); i++) {
			ModelData currentElm = store.get(i);
			if ((Boolean) currentElm.get("edited")) {
				elements.add(elementEditor.retriveReportElement());
			} else {
				elements.add((ReportElement) currentElm.get("element"));
			}
		}
		return elements;
	}

	public void unEditElements() {
		for (ModelData elm : view.getReportElements().getModels()) {
			elm.set("edited", false);
		}
	}

	public void createSubscribeForm() {
		form = new SubscribeForm();
		form.setReadOnlyTitle(view.getReport().getTitle());
		form.setEmailList(view.getReport().getSubscribers());
		if(view.getReport().getFrequency() != null && view.getReport().getDay() != null ){
			form.setFrequency(view.getReport().getFrequency(), view.getReport().getDay());
		}

		dialog = new FormDialogImpl(form);
		dialog.setWidth(450);
		dialog.setHeight(400);
		dialog.setHeading(I18N.CONSTANTS.SubscribeTitle());

		dialog.show(new FormDialogCallback() {
			@Override
			public void onValidated() {
				if (form.validListField()) {
					subscribeEmail(view.getReport().getId());
				} else {
					MessageBox.alert(I18N.CONSTANTS.error(),
							I18N.MESSAGES.noEmailAddress(), null);
				}
			}
		});
	}

	public void subscribeEmail(int reportId) {

		CreateSubscribe sub = new CreateSubscribe();
		sub.setReportTemplateId(reportId);
		sub.setEmailsList(form.getEmailList());
		sub.setReportFrequency(form.getReportFrequency());
		sub.setDay(form.getDay());

		service.execute(sub, dialog, new AsyncCallback<VoidResult>() {
			public void onFailure(Throwable caught) {
				dialog.onServerError();
			}

			public void onSuccess(VoidResult result) {
				dialog.hide();
				loadReport(view.getReport().getId());
				
			}
		});
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
