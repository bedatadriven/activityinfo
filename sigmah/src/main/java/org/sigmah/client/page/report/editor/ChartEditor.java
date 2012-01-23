package org.sigmah.client.page.report.editor;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.charts.AbstractChart;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.Store;
import com.google.inject.Inject;

public class ChartEditor extends AbstractChart implements AbstractEditor {

	PivotChartReportElement element;

	@Inject
	public ChartEditor(EventBus eventBus, Dispatcher service) {
		super(eventBus, service);

		addIndicatorPanelListener();
		addCategoryComboListner();
	}

	private void addIndicatorPanelListener() {
		indicatorPanel.addListenerToStore(Store.DataChanged,
				new Listener<BaseEvent>() {

					@Override
					public void handleEvent(BaseEvent be) {

						if (element != null) {
							List<Integer> indicators = element.getIndicators();
							for (Integer id : indicators) {
								indicatorPanel.setSelection(id, true);
							}
						}

					}
				});
	}

	private void addCategoryComboListner() {
		categoryCombo.getStore().addListener(Store.DataChanged,
				new Listener<BaseEvent>() {

					@Override
					public void handleEvent(BaseEvent be) {

						if (element != null
								&& element.getCategoryDimensions() != null
								&& element.getCategoryDimensions().size() > 0) {

							categoryCombo.setValue((Dimension) element.getCategoryDimensions().get(0));
						}

					}
				});

	}

	public void bindElement(final PivotChartReportElement element) {

		this.element = element;

		typeGroup.setSelection(element.getType());
		updateLabels();

		if (element.getSeriesDimension() != null && element.getSeriesDimension().size() > 0) {
			legendCombo.setValue((Dimension) element.getSeriesDimension().get(0));
		}

		load();

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
