package org.activityinfo.client.report.editor.chart;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.page.report.HasReportElement;
import org.activityinfo.shared.report.model.PivotChartReportElement;

import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public class ChartPivotBar extends ToolBar implements HasReportElement<PivotChartReportElement> {
	
	
	private DimensionComboBoxSet comboBoxes;

	
	public ChartPivotBar(EventBus eventBus, Dispatcher dispatcher) {
		comboBoxes = new DimensionComboBoxSet(eventBus, dispatcher);
		
		add(comboBoxes.getCategoryLabel());
		add(comboBoxes.getCategoryCombo());

		add(new FillToolItem());

		add(comboBoxes.getSeriesLabel());
		add(comboBoxes.getSeriesCombo());
	}
	
	@Override
	public void bind(PivotChartReportElement model) {
		this.comboBoxes.bind(model);
	}
	
	public PivotChartReportElement getModel() {
		return this.comboBoxes.getModel();
	}

	@Override
	public void disconnect() {
		this.comboBoxes.disconnect();
	}

}
