package org.sigmah.client.page.report;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageElement;
import org.sigmah.client.page.charts.ChartPage;
import org.sigmah.client.page.map.MapPage;
import org.sigmah.client.page.report.editor.AbstractEditor;
import org.sigmah.client.page.report.editor.ChartEditor;
import org.sigmah.client.page.table.PivotPresenter;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.ReportElement;
import org.sigmah.shared.report.model.TableElement;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ReportElementEditor {

	private final Dispatcher service;

	private Provider<PivotPresenter> pivotPageProvider;
	private Provider<MapPage> mapPageProvider;
	private Provider<ChartEditor> chartEditorProvider;

	private ReportElement reportElement;
	private Page page;
	private AbstractEditor editor;

	@Inject
	public ReportElementEditor(Dispatcher service,
			Provider<PivotPresenter> pivotPageProvider,
			Provider<MapPage> mapPageProvider,
			Provider<ChartEditor> chartEditorProvider) {

		this.service = service;
		this.pivotPageProvider = pivotPageProvider;
		this.mapPageProvider = mapPageProvider;
		this.chartEditorProvider = chartEditorProvider;
	}

	public Object createEditor(ReportElement reportElement) {

		this.reportElement = reportElement;
		createEditor();

		return page.getWidget();
	}

	private void createEditor() {
		if (this.reportElement instanceof PivotChartReportElement) {
			createChart();
			//((ChartPage)page).bindReportElement((PivotChartReportElement)this.reportElement);
			
		} else if (this.reportElement instanceof PivotTableReportElement) {
			createTable();
			((PivotPresenter)page).bindReportElement((PivotTableReportElement)this.reportElement);
			
		} else if (this.reportElement instanceof MapReportElement) {
			createMap();
			((MapPage)page).bindReportElement((MapReportElement)this.reportElement);
			
		} else if (this.reportElement instanceof TableElement) {
			// not sure
		} else {
			throw new RuntimeException("Unknown element type "
					+ reportElement.getClass().getName());
		}
	}

	public Object createChart() {
		editor = chartEditorProvider.get();
		return editor.getWidget();
	}

	public Object createMap() {
		page = mapPageProvider.get();
		return getWidget();
	}

	public Object createTable() {
		page = pivotPageProvider.get();
		return getWidget();
	}

	public Object getWidget() {
		return page.getWidget();
	}
	
	public ReportElement retriveReportElement(){		
		return ((PageElement) page).retriveReportElement();
	}

}
