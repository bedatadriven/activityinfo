package org.sigmah.client.page.report.editor;

import org.sigmah.client.page.charts.ChartEditor;
import org.sigmah.client.page.map.MapEditor;
import org.sigmah.client.page.table.PivotTableEditor;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class EditorProvider {

	private final Provider<ChartEditor> chartEditor;
	private final Provider<MapEditor> mapEditor;
	private final Provider<PivotTableEditor> pivotEditor;
	private final Provider<CompositeEditor> compositeEditor;

	@Inject
	public EditorProvider(Provider<ChartEditor> chartEditor, Provider<MapEditor> mapEditor, 
			Provider<PivotTableEditor> pivotEditor, Provider<CompositeEditor> compositeEditor) {
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
