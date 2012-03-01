package org.sigmah.client.report.editor.chart;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.report.editor.ReportElementEditor;
import org.sigmah.client.report.view.ChartOFCView;
import org.sigmah.client.report.view.PivotGridPanel;
import org.sigmah.client.report.view.ReportViewBinder;
import org.sigmah.shared.command.RenderElement.Format;
import org.sigmah.shared.report.model.PivotChartReportElement;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.inject.Inject;

public class ChartEditor extends LayoutContainer implements ReportElementEditor<PivotChartReportElement>  {

	private final EventBus eventBus;
	private final Dispatcher dispatcher;
	 
	private ActionToolBar toolBar;

	private ChartTypeGroup typeGroup;
	private ChartPivotBar pivotBar;
	private PivotFilterPanel filterPane;
	
	private ReportViewBinder preview;
	private ChartOFCView chartView;
	
	private ContentPanel center;
	private PivotGridPanel gridPanel;

	private PivotChartReportElement model = new PivotChartReportElement();

	@Inject
	public ChartEditor(EventBus eventBus, Dispatcher service) {
		this.eventBus = eventBus;
		this.dispatcher = service;

		setLayout(new BorderLayout());

		createWest();
		createCenter();
		createToolBar();
		createChartPane();
		createDimBar();
		createGridPane();
	}

	
	private void createWest() {

		filterPane = new PivotFilterPanel(eventBus, dispatcher);
		
		BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST,
				0.30f);
		west.setCollapsible(true);
		west.setSplit(true);
		west.setMargins(new Margins(0, 5, 0, 0));

		add(filterPane, west);
	}

	private void createCenter() {

		center = new ContentPanel(new BorderLayout());
		center.setHeaderVisible(false);

		add(center, new BorderLayoutData(Style.LayoutRegion.CENTER));
	}

	private void createToolBar() {
		toolBar = new ActionToolBar();

		typeGroup = new ChartTypeGroup(eventBus);

		toolBar.add(new LabelToolItem(I18N.CONSTANTS.chartType()));
		toolBar.add(typeGroup.getButtons());


		center.setTopComponent(toolBar);
	}

	private void createChartPane() {
		chartView  = new ChartOFCView();
		preview = new ReportViewBinder(eventBus, dispatcher, chartView);
		center.add(chartView, new BorderLayoutData(Style.LayoutRegion.CENTER));
	}

	private void createGridPane() {
		BorderLayoutData south = new BorderLayoutData(Style.LayoutRegion.SOUTH,
				0.30f);
		south.setCollapsible(true);
		south.setSplit(true);
		south.setMargins(new Margins(5, 0, 0, 0));

		gridPanel = new PivotGridPanel();
		gridPanel.setHeading("Table");

		center.add(gridPanel, south);
	}

	private void createDimBar() {
		pivotBar = new ChartPivotBar(eventBus, dispatcher);

		chartView.setBottomComponent(pivotBar);
	}

	@Override
	public void bind(PivotChartReportElement model) {
		this.model = model;
		typeGroup.bind(model);
		pivotBar.bind(model);
		filterPane.bind(model);
		preview.bind(model);
	}
	
	@Override
	public PivotChartReportElement getModel() {
		return model;
	}

	@Override
	public Component getWidget() {
		return this;
	}


	@Override
	public List<Format> getExportFormats() {
		return Arrays.asList(Format.PowerPoint, Format.Word, Format.PDF, Format.PNG);
	}

}
