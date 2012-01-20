package org.sigmah.client.page.report;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.report.editor.AbstractEditor;
import org.sigmah.client.page.report.editor.ChartEditor;
import org.sigmah.client.page.report.editor.MapEditor;
import org.sigmah.client.page.report.editor.PivotEditor;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.ReportElement;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ReportElementEditor {

	private final Dispatcher service;

	private Provider<PivotEditor> pivotEditorProvider;
	private Provider<MapEditor> mapEditorProvider;
	private Provider<ChartEditor> chartEditorProvider;

	private ReportElement reportElement;
	private AbstractEditor editor;

	@Inject
	public ReportElementEditor(Dispatcher service,
			Provider<PivotEditor> pivotEditorProvider,
			Provider<MapEditor> mapEditorProvider,
			Provider<ChartEditor> chartEditorProvider) {

		this.service = service;
		this.pivotEditorProvider = pivotEditorProvider;
		this.mapEditorProvider = mapEditorProvider;
		this.chartEditorProvider = chartEditorProvider;
	}

	public Object createEditor(ReportElement reportElement) {

		this.reportElement = reportElement;
		createEditor();

		return editor.getWidget();
	}

	private void createEditor() {
		if (this.reportElement instanceof PivotChartReportElement) {
			createChart();
		} else if (this.reportElement instanceof PivotTableReportElement) {
			createTable();
		} else if (this.reportElement instanceof MapReportElement) {
			createMap();
		} else {
			throw new RuntimeException("Unknown element type "
					+ reportElement.getClass().getName());
		}
		
		editor.bindReportElement(reportElement);
	}

	public Object createChart() {
		editor = chartEditorProvider.get();
		return getWidget();
	}

	public Object createMap() {
		editor = mapEditorProvider.get();
		return getWidget();
	}

	public Object createTable() {
		editor = pivotEditorProvider.get();
		return getWidget();
	}

	public Object getWidget() {
		return editor.getWidget();
	}
	
	public ReportElement retriveReportElement(){		
		return editor.getReportElement();
	}

}
