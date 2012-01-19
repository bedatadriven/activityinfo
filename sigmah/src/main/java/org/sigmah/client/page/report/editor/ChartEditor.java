package org.sigmah.client.page.report.editor;

import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.charts.AbstractChart;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.google.inject.Inject;

public class ChartEditor extends AbstractChart implements AbstractEditor {

	@Inject
	public ChartEditor(EventBus eventBus, Dispatcher service) {
		super(eventBus, service);
	}
	
	public void bindElement(final PivotChartReportElement element) {

		new DelayedTask(new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				typeGroup.setSelection(element.getType());
				updateLabels();

				if (element.getCategoryDimensions() != null
						&& element.getCategoryDimensions().size() > 0) {
					categoryCombo.setValue((Dimension) element
							.getCategoryDimensions().get(0));
				}

				List<Integer> indicators = element.getIndicators();
				for (Integer id : indicators) {
					indicatorPanel.setSelection(id, true);
				}

				if (element.getSeriesDimension() != null
						&& element.getSeriesDimension().size() > 0) {
					legendCombo.setValue((Dimension) element
							.getSeriesDimension().get(0));
				}

				load();
			}
		}).delay(10000);

	}
	
	@Override
	public void bindReportElement(ReportElement element) {
		bindElement((PivotChartReportElement) element);
	}

	@Override
	public ReportElement getReportElement() {
		return getChartElement();
	}

	@Override
	public Object getWidget() {
		return this;
	}

}
