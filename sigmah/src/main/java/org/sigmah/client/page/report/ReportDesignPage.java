package org.sigmah.client.page.report;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.dto.ReportDefinitionDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Scroll;
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
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;

public class ReportDesignPage extends ContentPanel implements
		ReportDesignPresenter.View {

	private final EventBus eventBus;
	private final Dispatcher service;

	private ToolBar toolBar;
	private ReportDesignPresenter presenter;
	private ContentPanel reportListPane;
	private ContentPanel reportPreviewPanel;
	private ListView<ReportDefinitionDTO> reportList;
	protected ListStore<ReportDefinitionDTO> store;
    private Html previewHtml;
	private Button reportPreview;
	private int currentReport;
	
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
		presenter.setReportListStore();
	}

	private void initializeComponent() {
		setLayout(new BorderLayout());
		setHeaderVisible(false);
	}

	public void createToolbar() {

		toolBar = new ToolBar();

		TextField<String> titleField = new TextField<String>();
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
		
		reportListPane = new ContentPanel();
		reportListPane.setHeading(I18N.CONSTANTS.reports());
		reportListPane.setLayout(layout);
	
		reportPreview = new Button(I18N.CONSTANTS.preview(), null,
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						if(currentReport != 0){
						presenter.generateReportPreview(currentReport);
						}
					}
				});
		reportPreview.setWidth(100);
		
		VBoxLayoutData buttonLayout = new VBoxLayoutData();
		buttonLayout.setMargins(new Margins(0, 0, 5, 0 ));
		reportListPane.add(reportPreview, buttonLayout);
		
		store = new ListStore<ReportDefinitionDTO>();

		reportList = new ListView<ReportDefinitionDTO>();
		reportList.setTemplate(getTemplate(GWT.getModuleBaseURL() + "image/"));
		reportList.setBorders(false);
		reportList.setStore(store);
		reportList.setItemSelector("dd");
		reportList.setOverStyle("over");
		reportList.addListener(Events.Select,
				new Listener<ListViewEvent<ReportDefinitionDTO>>() {

					@Override
					public void handleEvent(
							ListViewEvent<ReportDefinitionDTO> event) {
						currentReport = event.getModel().getId();
					}
				});
		
		VBoxLayoutData listLayout = new VBoxLayoutData();
		reportListPane.add(reportList, listLayout);
		
		BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST);
		west.setMinSize(250);
		west.setSize(250);
		west.setCollapsible(true);
		west.setSplit(true);
		west.setMargins(new Margins(0, 0, 0, 0));

		add(reportListPane, west);

	}

	public void reportPreviewPane() {

		reportPreviewPanel = new ContentPanel();
		reportPreviewPanel.setHeaderVisible(true);
		reportPreviewPanel.setScrollMode(Scroll.AUTO);
		reportPreviewPanel.setLayout(new FitLayout());
		
		previewHtml = new Html();
        previewHtml.addStyleName("report");
        reportPreviewPanel.add(previewHtml);
		
		add(reportPreviewPanel, new BorderLayoutData(Style.LayoutRegion.CENTER));

	}

	@Override
	public void setReportListStore(ReportDefinitionDTO dto) {
		store.add(dto);
		
	}
	@Override
	public ListStore<ReportDefinitionDTO> getReportListStore(){
		return store;
	}

	private native String getTemplate(String base) /*-{
		return [ '<dl><tpl for=".">', '<dd>',
				'<img src="' + base + 'report.png" title="{title}">',
				'<span>{title}</span>', '</tpl>',
				'<div style="clear:left;"></div></dl>' ].join("");

	}-*/;

	@Override
	public void setPreviewHtml(String html) {
        previewHtml.setHtml(html);
    }

	@Override
	public AsyncMonitor getLoadingMonitor() {
		 return new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading());
	}
}
