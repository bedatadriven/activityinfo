package org.sigmah.client.page.report;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.filter.IndicatorTreePanel;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.dto.ReportDefinitionDTO;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.ReportElement;
import org.sigmah.shared.report.model.TableElement;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
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
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ReportDesignPage extends ContentPanel implements
		ReportDesignPresenter.View {

	private final EventBus eventBus;
	private final Dispatcher service;

	private ToolBar toolBar;
	private TextField<String> titleField;
	private ReportDesignPresenter presenter;
	private ContentPanel elementListPane;
	private LayoutContainer center;
	private ContentPanel reportPreviewPanel;
	private ListView<ModelData> reportElements;
	protected ListStore<ModelData> store;
	private Html previewHtml;
	private Button reportPreview;
	private ReportDefinitionDTO selectedReport;
	private IndicatorTreePanel indicatorPanel;

	@Inject
	public ReportDesignPage(EventBus eventBus, Dispatcher service) {
		this.eventBus = eventBus;
		this.service = service;

	}

	@Override
	public void init(ReportDesignPresenter presenter) {
		this.presenter = presenter;
		initializeComponent();
		createToolbar();
		createListPane();
		reportPreviewPane();

	}

	private void initializeComponent() {
		setLayout(new BorderLayout());
		setHeaderVisible(false);

		previewHtml = new Html();
		previewHtml.addStyleName("report");

	}

	public void createToolbar() {

		toolBar = new ToolBar();

		titleField = new TextField<String>();
		toolBar.add(titleField);

		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (presenter != null && ce.getButton().getItemId() != null) {
					presenter.onUIAction(ce.getButton().getItemId());
				}
			}
		};

		Button addChart = new Button(I18N.CONSTANTS.addChart(), null, listener);
		addChart.setItemId(UIActions.ADDCHART);
		toolBar.add(addChart);

		Button addMap = new Button(I18N.CONSTANTS.addMap(), null, listener);
		addMap.setItemId(UIActions.ADDMAP);
		toolBar.add(addMap);

		Button addTable = new Button(I18N.CONSTANTS.addTable(), null, listener);
		addTable.setItemId(UIActions.ADDTABLE);
		toolBar.add(addTable);

		setTopComponent(toolBar);
	}

	public void createListPane() {

		VBoxLayout layout = new VBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);

		elementListPane = new ContentPanel();
		elementListPane.setHeading(I18N.CONSTANTS.reports());
		elementListPane.setLayout(layout);

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

		store = new ListStore<ModelData>();

		reportElements = new ListView<ModelData>();
		reportElements.setTemplate(getTemplate(GWT.getModuleBaseURL() + "image/"));
		reportElements.setBorders(false);
		reportElements.setStore(store);
		reportElements.setItemSelector("dd");
		reportElements.setOverStyle("over");
		reportElements.addListener(Events.Select,
				new Listener<ListViewEvent<ModelData>>() {

					@Override
					public void handleEvent(
							ListViewEvent<ModelData> event) {
//						selectedReport = event.getModel();
//						reportPagePresenter.go(selectedReport);
//						addToPreviewPanel(
//								(Widget) reportPagePresenter.getWidget(),
//								selectedReport.getTitle());
					}
				});

		VBoxLayoutData listLayout = new VBoxLayoutData();
		elementListPane.add(reportElements, listLayout);

		BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST, 0.30f);
		west.setMinSize(250);
		west.setSize(250);
		west.setCollapsible(true);
		west.setSplit(true);
		west.setMargins(new Margins(0, 5, 0, 0));

		add(elementListPane, west);

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

	private void addToPreviewPanel(Widget w, String title) {
		reportPreviewPanel.setHeading(I18N.CONSTANTS.reportPreview() + ": "
				+ title);
		reportPreviewPanel.removeAll();
		reportPreviewPanel.add(w);
		reportPreviewPanel.layout();
	}

	@Override
	public void setReportElement(ReportDefinitionDTO dto) {
		List<ModelData> reportElements = new ArrayList<ModelData>();
		for(ReportElement element : dto.getReport().getElements()) {
			reportElements.add(addReportElement(element));
		}
		store.removeAll();
		store.add(reportElements);
	}

	private ModelData addReportElement(ReportElement element){
		BaseModelData elm = new BaseModelData();
		elm.set("element", element);
		if(element.getTitle() == null || element.getTitle().equals("")){
			 if (element instanceof PivotChartReportElement) {
				 	elm.set("elementTitle", "Chart Element" );
		        } else if (element instanceof PivotTableReportElement) {
		        	elm.set("elementTitle", "Pivot Table Element" );
		        } else if (element instanceof MapReportElement) {
		        	elm.set("elementTitle", "Map Element" );
		        } 
		}else{
			elm.set("elementTitle", element.getTitle() );
		}	
		return elm;
	}
	
	@Override
	public ListStore<ModelData> getReportElements() {
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
		titleField.setValue(dto.getTitle());
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
	
}
