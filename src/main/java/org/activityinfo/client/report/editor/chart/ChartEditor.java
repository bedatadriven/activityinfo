package org.activityinfo.client.report.editor.chart;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.common.toolbar.ActionToolBar;
import org.activityinfo.client.page.report.editor.ReportElementEditor;
import org.activityinfo.client.report.view.ChartOFCView;
import org.activityinfo.client.report.view.PivotGridPanel;
import org.activityinfo.client.report.view.ReportViewBinder;
import org.activityinfo.shared.command.RenderElement.Format;
import org.activityinfo.shared.report.model.PivotChartReportElement;

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
		chartView.bindTable(gridPanel);

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
	public void disconnect() {
		typeGroup.disconnect();
		pivotBar.disconnect();
		filterPane.disconnect();
		preview.disconnect();
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
		return Arrays.asList(Format.PowerPoint, Format.Word, Format.PDF);
	}

}
