package org.activityinfo.client.page.report.editor;

import org.activityinfo.client.report.editor.chart.ChartEditor;
import org.activityinfo.client.report.editor.map.MapEditor;
import org.activityinfo.client.report.editor.pivotTable.PivotTableEditor;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.ReportElement;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class EditorProvider {

	private final Provider<ChartEditor> chartEditor;
	private final Provider<MapEditor> mapEditor;
	private final Provider<PivotTableEditor> pivotEditor;
	private final Provider<CompositeEditor2> compositeEditor;

	@Inject
	public EditorProvider(Provider<ChartEditor> chartEditor, Provider<MapEditor> mapEditor, 
			Provider<PivotTableEditor> pivotEditor, Provider<CompositeEditor2> compositeEditor) {
		this.chartEditor = chartEditor;
		this.mapEditor = mapEditor;
		this.pivotEditor = pivotEditor;
		this.compositeEditor = compositeEditor;
	}
	
	public ReportElementEditor create(ReportElement model) {
		if(model instanceof PivotChartReportElement) {
			return chartEditor.get();
		} else if(model instanceof PivotTableReportElement) {
			return pivotEditor.get();
		} else if(model instanceof MapReportElement) {
			return mapEditor.get();
		} else if(model instanceof Report) {
			return compositeEditor.get();
		} else {
			throw new IllegalArgumentException(model.getClass().getName());
		}
	}
}
