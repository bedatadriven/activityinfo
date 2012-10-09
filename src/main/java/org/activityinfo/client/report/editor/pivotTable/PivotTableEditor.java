package org.activityinfo.client.report.editor.pivotTable;

import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.event.PivotCellEvent;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.report.ReportEventHelper;
import org.activityinfo.client.page.report.editor.ReportElementEditor;
import org.activityinfo.client.report.editor.chart.PivotFilterPanel;
import org.activityinfo.client.report.view.DrillDownEditor;
import org.activityinfo.client.report.view.PivotGridPanel;
import org.activityinfo.client.report.view.ReportViewBinder;
import org.activityinfo.client.util.date.DateUtilGWTImpl;
import org.activityinfo.client.util.state.StateProvider;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.RenderElement.Format;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.model.PivotTableReportElement;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.inject.Inject;

public class PivotTableEditor extends LayoutContainer implements ReportElementEditor<PivotTableReportElement>{

	private final EventBus eventBus;
	private final Dispatcher service;
	private final StateProvider stateMgr;
	
	private final ReportEventHelper events;
		
	private PivotTrayPanel pivotPanel;
	private PivotFilterPanel filterPane;
	private ReportViewBinder<PivotContent, PivotTableReportElement> viewBinder;
	
	private DimensionPruner pruner;
	
	private LayoutContainer center;
	private PivotGridPanel gridPanel;
	
	private PivotTableReportElement model; 

	@Inject
	public PivotTableEditor(EventBus eventBus, Dispatcher service,
			StateProvider stateMgr) {
		this.eventBus = eventBus;
		this.service = service;
		this.stateMgr = stateMgr;
		
		initializeComponent();

		createPane();
		createFilterPane();
		createGridContainer();

		this.pruner = new DimensionPruner(eventBus, service);

		events = new ReportEventHelper(eventBus, this);


//		initialDrillDownListener = new Listener<PivotCellEvent>() {
//			@Override
//			public void handleEvent(PivotCellEvent be) {
//				createDrilldownPanel(be);
//			}
//		};
//		eventBus.addListener(AppEvents.DRILL_DOWN, initialDrillDownListener);

	}

	private void initializeComponent() {
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setEnableState(true);
		setStateId("pivotPage");
		setLayout(borderLayout);
	}

	private void createPane() {

		pivotPanel = new PivotTrayPanel(eventBus, service);

		BorderLayoutData east = new BorderLayoutData(Style.LayoutRegion.EAST);
		east.setCollapsible(true);
		east.setSplit(true);
		east.setMargins(new Margins(0, 5, 0, 0));

		add(pivotPanel, east);
	}

	private void createFilterPane() {
		filterPane = new PivotFilterPanel(eventBus, service);
		filterPane.applyBaseFilter(new Filter());

		BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST);
		west.setMinSize(250);
		west.setSize(250);
		west.setCollapsible(true);
		west.setSplit(true);
		west.setMargins(new Margins(0, 0, 0, 0));
		add(filterPane, west);
	}

	private void createGridContainer() {
		center = new LayoutContainer();
		center.setLayout(new BorderLayout());
		add(center, new BorderLayoutData(Style.LayoutRegion.CENTER));

		gridPanel = new PivotGridPanel();
		gridPanel.setHeaderVisible(true);
		gridPanel.setHeading(I18N.CONSTANTS.preview());

		center.add(gridPanel, new BorderLayoutData(
				Style.LayoutRegion.CENTER));
		
		viewBinder = ReportViewBinder.create(eventBus, service, gridPanel);
	}

	private void createDrilldownPanel(PivotCellEvent event) {
		BorderLayoutData layout = new BorderLayoutData(Style.LayoutRegion.SOUTH);
		layout.setSplit(true);
		layout.setCollapsible(true);

		DrillDownEditor drilldownEditor = new DrillDownEditor(eventBus,
				service, stateMgr, new DateUtilGWTImpl());
		drilldownEditor.onDrillDown(event);

		center.add(drilldownEditor.getGridPanel(), layout);

		// disconnect our initial drilldown listener;
		// subsequent events will be handled by the DrillDownEditor's listener
		//eventBus.removeListener(AppEvents.DRILL_DOWN, initialDrillDownListener);

		layout();
	}


	public AsyncMonitor getMonitor() {
		return new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading());
	}

	@Override
	public PivotTableReportElement getModel() {
		return model;
	}

	@Override
	public void bind(PivotTableReportElement model) {
		this.model = model;
		pivotPanel.bind(model);
		filterPane.bind(model);
		viewBinder.bind(model);
		pruner.bind(model);
	}
	
	@Override
	public void disconnect() {
		pivotPanel.disconnect();
		filterPane.disconnect();
		viewBinder.disconnect();
		pruner.disconnect();
	}

	@Override
	public Component getWidget() {
		return this;
	}

	@Override
	public List<Format> getExportFormats() {
		return Arrays.asList(Format.Excel, Format.Word, Format.PDF);
	}

	
	
	
}
