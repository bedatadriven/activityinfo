package org.sigmah.client.page.report;

import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.filter.IndicatorTreePanel;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.report.resources.ReportResources;
import org.sigmah.shared.dto.ReportDefinitionDTO;
import org.sigmah.shared.report.model.TableElement;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.EditorEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ReportDesignPage extends ContentPanel implements ReportDesignPresenter.View {

	private final EventBus eventBus;
	private final Dispatcher service;

	private ReportDesignPresenter presenter;
	private ContentPanel elementListPane;
	private LayoutContainer center;
	private ContentPanel reportPreviewPanel;
	private ListView<ReportElementModel> reportElements;
	protected ListStore<ReportElementModel> store;
	private Html previewHtml;
	private Button reportPreview;
	private ReportDefinitionDTO selectedReport;
	private IndicatorTreePanel indicatorPanel;

	
	private boolean reportEdited;
	private ReportBar reportBar;
	
	@Inject
	public ReportDesignPage(EventBus eventBus, Dispatcher service) {
		this.eventBus = eventBus;
		this.service = service;
		
		ReportResources.INSTANCE.style().ensureInjected();
	}

	@Override
	public void init(ReportDesignPresenter presenter) {
		this.presenter = presenter;
		initializeComponent();
		createToolbar();
		createElementListPane();
		reportPreviewPane();
	}

	private void initializeComponent() {
		setLayout(new BorderLayout());
		setHeaderVisible(false);

		previewHtml = new Html();
		previewHtml.addStyleName("report");

	}

	public void createToolbar() {
		reportBar = new ReportBar();
		BorderLayoutData reportBarLayout = new BorderLayoutData(LayoutRegion.NORTH);
		reportBarLayout.setSize(35);
		add(reportBar, reportBarLayout);
				
		reportBar.addTitleEditCompleteListener(new Listener<EditorEvent>() {
			@Override
			public void handleEvent(EditorEvent be) {
				presenter.updateReport(selectedReport.getId(), (String)be.getValue(), null);
				reportBar.setReportTitle((String)be.getValue());
			}
		});
		
		reportBar.getSaveButton().addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				if(presenter != null) {
					presenter.onUIAction(UIActions.SAVE);
				}
			}
		});
	}

	public void createElementListPane() {


		elementListPane = new ContentPanel();
		elementListPane.setHeading(I18N.CONSTANTS.reportElements());
		elementListPane.setLayout(createVBoxLayout());

		createReportPreviewButton();
		
		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (presenter != null && ce.getButton().getItemId() != null) {
					presenter.onUIAction(ce.getButton().getItemId());
				}
			}
		};

		Button addChart = new Button(I18N.CONSTANTS.addChart(), IconImageBundle.ICONS.barChart(), listener);
		addChart.setItemId(UIActions.ADDCHART);
		elementListPane.add(addChart);

		Button addMap = new Button(I18N.CONSTANTS.addMap(),  IconImageBundle.ICONS.map(), listener);
		addMap.setItemId(UIActions.ADDMAP);
		elementListPane.add(addMap);

		Button addTable = new Button(I18N.CONSTANTS.addTable(), IconImageBundle.ICONS.table(), listener);
		addTable.setItemId(UIActions.ADDTABLE);
		elementListPane.add(addTable);

		creteReportElementListView();

		add(elementListPane, createBorderWestLayout());
	}

	private VBoxLayout createVBoxLayout(){
		VBoxLayout layout = new VBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);
		
		return layout;
	}
	
	private void createReportPreviewButton(){
		reportPreview = new Button(I18N.CONSTANTS.preview(), null,
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						if (selectedReport != null) {
							presenter.generateReportPreview(selectedReport);
						}
					}
				});
		reportPreview.setWidth(100);

		VBoxLayoutData buttonLayout = new VBoxLayoutData();
		buttonLayout.setMargins(new Margins(0, 0, 5, 0));
		
		elementListPane.add(reportPreview, buttonLayout);
	}
	
	private void creteReportElementListView(){

		store = new ListStore<ReportElementModel>();
		
		reportElements = new ListView<ReportElementModel>();
		reportElements.setTemplate(getTemplate(GWT.getModuleBaseURL() + "image/"));
		reportElements.setBorders(false);
		reportElements.setStore(store);
		reportElements.setItemSelector("dd");
		reportElements.setOverStyle("over");
		
		reportElements.addListener(Events.Select,
				new Listener<ListViewEvent<ReportElementModel>>() {

					@Override
					public void handleEvent(ListViewEvent<ReportElementModel> event) {
						if(event.getModel().getReportElement()instanceof TableElement){
							Window.alert("Editing this type of element is not supported");
						} else if(event.getModel().getId() != presenter.getElementInViewId()){
							loadElementEditor(event.getModel());	
						}	
					}
				});


		VBoxLayoutData listLayout = new VBoxLayoutData();
		elementListPane.add(reportElements, listLayout);
	}
	
	private BorderLayoutData createBorderWestLayout(){
		BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST, 0.30f);
		west.setMinSize(250);
		west.setSize(250);
		west.setCollapsible(true);
		west.setSplit(true);
		west.setMargins(new Margins(0, 5, 0, 0));
		
		return west;
	}
	
	public void reportPreviewPane() {
		center = new LayoutContainer();
		center.setLayout(new BorderLayout());
		add(center, new BorderLayoutData(Style.LayoutRegion.CENTER));

		reportPreviewPanel = new ContentPanel();
		reportPreviewPanel.setHeaderVisible(true);
		reportPreviewPanel.setHeading(I18N.CONSTANTS.reportPreview());
		reportPreviewPanel.setScrollMode(Scroll.AUTO);
		reportPreviewPanel.setLayoutData(new FitData(new Margins(5, 5, 5, 5)));
		reportPreviewPanel.setLayout(new FitLayout());

//		reportPreviewPanel.add((Widget) reportPagePresenter.getWidget());

		center.add(reportPreviewPanel, new BorderLayoutData(Style.LayoutRegion.CENTER));

	}

	public void addToCenterPanel(Widget w, String title){
		addToPreviewPanel(w,title);
	}
	
	private void addToPreviewPanel(Widget w, String title) {
		reportPreviewPanel.setHeading(title);
		reportPreviewPanel.removeAll();
		reportPreviewPanel.add(w);
		reportPreviewPanel.layout();
	}

	
	@Override
	public void setStore(List<ReportElementModel> elements){
		store.removeAll();
		store.add(elements);
	}
	
	@Override
	public void addElementInStore(ReportElementModel element){
		store.add(element);
	}
	
	private void loadElementEditor(ReportElementModel model){
		
		reportBar.getSaveButton().setEnabled(true);
		model.setEdited(true);
	
		presenter.loadElementInEditor(model);
		reportElements.getSelectionModel().select(1, false);
	}
	
	public void setReportEdited(boolean edited){
		reportEdited = edited;
	}
	
	public boolean reportEdited(){
		return reportEdited;
	}
	
	@Override
	public ListStore<ReportElementModel> getListViewStore() {
		return store;
	}

	private native String getTemplate(String base) /*-{
		return [ '<dl><tpl for=".">', '<dd>',
				'<img src="' + base + 'report.png" title="{elementTitle}">',
				'<span>{elementTitle}</span>', '</tpl>',
				'<div style="clear:left;"></div></dl>' ].join("");

	}-*/;

	@Override
	public void setPreview() {

	}

	@Override
	public void setReport(ReportDefinitionDTO dto){
		selectedReport = dto;
		reportBar.setReportTitle(dto.getTitle());
	}
	
	@Override
	public void setPreviewHtml(String html, String heading) {
		previewHtml.setHtml(html);
		addToPreviewPanel(previewHtml, heading);
	}
	

	@Override
	public AsyncMonitor getLoadingMonitor() {
		return new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading());
	}

	@Override
	public ReportDefinitionDTO getReport() {	
		return selectedReport;
	}
	
	@Override
	public Button getSaveButton(){
		return reportBar.getSaveButton();
	}
}
