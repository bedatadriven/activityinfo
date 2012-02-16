package org.sigmah.client.page.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sigmah.client.AppEvents;
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
import org.sigmah.shared.report.model.TableElement;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
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
	
	private int mapCounter, pivotCounter, chartCounter, tableCounter, id =  0;
	private int current = -1;

	@ImplementedBy(ReportDesignPage.class)
	public interface View {
		void init(ReportDesignPresenter presenter);

		void setStore(List<ReportElementModel> elements);

		void setPreviewHtml(String html, String heading);

		AsyncMonitor getLoadingMonitor();

		ListStore<ReportElementModel> getListViewStore();

		void setPreview();

		void setReport(ReportDefinitionDTO dto);

		void addToCenterPanel(Widget w, String title);

		ReportDefinitionDTO getReport();

		void addElementInStore(ReportElementModel element);

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
		
		addReportChangeListener();
	}
	
	private void addReportChangeListener(){
		eventBus.addListener(AppEvents.REPORT_CHANGED, new Listener<BaseEvent>() {
            @Override
			public void handleEvent(BaseEvent be) {
            	updateCurrentElementInStore();
            }
        });
	}

	public void go(int reportId) {
		// TODO if report id is not valid one, should create new report and dont send command to server.
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

		service.execute(new GetReport(reportId), view.getLoadingMonitor(),
				new AsyncCallback<ReportTemplateResult>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO show appropriate message.
					}

					@Override
					public void onSuccess(ReportTemplateResult result) {
						resetCounter();
						
						for (ReportDefinitionDTO report : result.getData()) {
							view.setStore(createReportElementList(report));
							view.setReport(report);
						}

					}
				});
	}

	private List<ReportElementModel> createReportElementList(ReportDefinitionDTO dto) {
		id = 0;
		List<ReportElementModel> elements = new ArrayList<ReportElementModel>();
		for(ReportElement element : dto.getReport().getElements()) {
			elements.add(addReportElement(element, false));
		}
		
		return elements;
	}
	
	private ReportElementModel addReportElement(ReportElement element, boolean edited){
		ReportElementModel model = new ReportElementModel();
		model.setEdited(edited);
		model.setReportElement(element);
		model.setId(getNextId());
		
		if(element.getTitle() == null || element.getTitle().equals("")){
		
			if (element instanceof PivotChartReportElement) {
				 	chartCounter++;
				 	model.setElementTitle(I18N.CONSTANTS.chart() + " " + chartCounter );
		        } else if (element instanceof PivotTableReportElement) {
		        	pivotCounter++;
		        	model.setElementTitle(I18N.CONSTANTS.pivotTable() + " " + pivotCounter );
		        } else if (element instanceof MapReportElement) {
		        	mapCounter++;
		        	model.setElementTitle(I18N.CONSTANTS.map() + " " + mapCounter);
		        } else if (element instanceof TableElement){
		        	tableCounter++;
		        	model.setElementTitle(I18N.CONSTANTS.table() + " " + tableCounter);
		        }
		}else{
			model.setElementTitle(element.getTitle());
		}
		
		return model;
	}
	
	private int getNextId(){
		id++;
		return id;
	}
	
	public void updateCurrentElementInStore(){
		for(ReportElementModel model :  view.getListViewStore().getModels()){
			if(model.getId() == current){
				model.setReportElement(elementEditor.retriveReportElement());
			}
		}
	}
	
	public void loadElementInEditor(ReportElementModel model){
		updateCurrentElementInStore();
		createEditor(model.getReportElement());
		current = model.getId();
	}
	
	private void createEditor(ReportElement e) {
		Widget w = (Widget) elementEditor.createEditor(e);
		view.addToCenterPanel(w, I18N.CONSTANTS.reportElementEditor());
	}

	public void addChart() {
		addNewElement(I18N.CONSTANTS.newChart(), (Widget) elementEditor.createChart());	
	}

	public void addTable() {
		addNewElement(I18N.CONSTANTS.newTable(), (Widget) elementEditor.createTable());
	}

	public void addMap() {
		addNewElement(I18N.CONSTANTS.newMap(), (Widget) elementEditor.createMap());		
	}

	private void addNewElement(String heading, Widget w) {
		current = ++id;
		updateCurrentElementInStore();
		addElementToView(w, heading);
	}
	
	private void addElementToView(Widget w, String heading){
		view.addToCenterPanel(w, heading);
		view.addElementInStore(addReportElement(elementEditor.retriveReportElement(), true));
	}
	
	private boolean isSaved(){
		
		if (view.getSaveButton().isEnabled()) {
			MessageBox.alert(I18N.CONSTANTS.error(), "Please save current report element.", null);
			return false;
		}
		return true;
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
			}
		});
	}

	public List<ReportElement> getReportElements() {
		updateCurrentElementInStore();
		
		List<ReportElement> elements = new ArrayList<ReportElement>();
		List<ReportElementModel> models = view.getListViewStore().getModels();	
		
		for(ReportElementModel m : models){
			elements.add((ReportElement) m.getReportElement());
		}

		return elements;
	}

	public void unEditElements() {
		for (ReportElementModel e : view.getListViewStore().getModels()) {
			e.setEdited(false);
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
	
	public void resetCounter(){
		mapCounter = 0;
		pivotCounter = 0; 
		chartCounter = 0;
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
	
	public int getElementInViewId(){
		return current;
	}
}
