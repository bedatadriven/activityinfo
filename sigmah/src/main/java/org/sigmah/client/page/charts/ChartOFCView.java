/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.charts;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.report.content.PivotChartContent;
import org.sigmah.shared.report.content.PivotTableData;
import org.sigmah.shared.report.model.PivotChartReportElement;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.axis.XAxis;
import com.extjs.gxt.charts.client.model.axis.YAxis;
import com.extjs.gxt.charts.client.model.charts.FilledBarChart;
import com.extjs.gxt.charts.client.model.charts.LineChart;
import com.extjs.gxt.charts.client.model.charts.PieChart;
import com.extjs.gxt.charts.client.model.charts.PieChart.Slice;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

/**
 * A view of the PivotChartElement using Open Flash Charts
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ChartOFCView extends ContentPanel implements ChartView {

	private static final String TRANSPARENT_BG = "-1";

	private Chart chart;
	private ChartModel chartModel;

	public ChartOFCView() {
		setHeaderVisible(false);
		setLayout(new FitLayout());
	}


	@Override
	protected void onRender(Element parent, int pos) {
		super.onRender(parent, pos);

		if(chartModel != null) {
			createChart();
		}
	}

	private void createChart() {
		chart = new Chart(GWT.getModuleBaseURL() + "/gxt224/chart/open-flash-chart.swf");
		chart.setBorders(false);
		chart.setChartModel(chartModel);

		if(!chart.isLoaded()) {
			this.el().mask(I18N.CONSTANTS.loading());
			chart.addListener(Events.Ready, new Listener<BaseEvent>() {
				@Override
				public void handleEvent(BaseEvent be) {
					chart.removeListener(Events.Ready, this);
					ChartOFCView.this.el().unmask();
				}
			});
		}
		add(chart, new MarginData(20));
	}

	private void setModel(ChartModel chartModel) {
		this.chartModel = chartModel;

		if(this.isRendered() && chart == null) {
			createChart();
			layout();
		} else {
			chart.setChartModel(chartModel);
		}       
	}


	/**
	 * Updates the view to the given PivotChartContent
	 *
	 * @param element
	 */
	@Override
	public void show(PivotChartReportElement element) {

		PivotChartContent content = element.getContent();
		PivotTableData table = element.getContent().getData();

		List<PivotTableData.Axis> categories = table.getRootRow().getLeaves();
		List<PivotTableData.Axis> series = table.getRootColumn().getLeaves();

		ChartModel cm = new ChartModel();
		cm.setBackgroundColour(TRANSPARENT_BG);
		cm.setNumDecimals(0);
		cm.setDecimalSeparatorComma(true);
		cm.setXAxis(createXAxis(categories));
		cm.setYAxis(createYAxis(content, table));

		switch(element.getType()) {
		case Bar:
		case ClusteredBar:
			addBarSeries(cm, categories, series);
			break;
		case Pie:
			addPieChart(cm, categories, series);
			break;
		case Line:
			addLineSeries(cm, categories, series);
			break;
		}

		setModel(cm);
	}


	private YAxis createYAxis(PivotChartContent content, PivotTableData table) {
		YAxis ya = new YAxis();
		double maxValue = table.getRootRow().getMaxValue();
		ya.setRange(content.getYMin(), maxValue);
		ya.setSteps(content.getYStep());
		ya.setGridColour("eeffee");
		ya.setColour("009900");
		return ya;
	}

	private XAxis createXAxis(List<PivotTableData.Axis> categories) {
		XAxis xa = new XAxis();
		List<String> xLabels = PivotTableData.flattenLabels(categories);
		xa.setLabels(xLabels);
		xa.getLabels().setColour("009900");
		xa.setGridColour("eeffee");
		xa.setColour("009900");
		if(xLabels.size() > 5) {
			xa.getLabels().setRotationAngle(45);
		}
		return xa;
	}


	private void addBarSeries(ChartModel cm,
			List<PivotTableData.Axis> categories, 
			List<PivotTableData.Axis> series) {

		for(PivotTableData.Axis s : series) { 

			FilledBarChart bchart = new FilledBarChart("6666ff", "000066");
			bchart.setTooltip("#x_label#<br>#val#");
			for (PivotTableData.Axis category : categories) {

				PivotTableData.Cell cell = category.getCell(s);
				bchart.addValues(cell == null ? 0 : cell.getValue());
			}
			cm.addChartConfig(bchart);
		}
	}

	private void addLineSeries(ChartModel cm,
			List<PivotTableData.Axis> categories,
			List<PivotTableData.Axis> series) {

		for(PivotTableData.Axis s : series) {
			LineChart lineChart = new LineChart();
			lineChart.setTooltip("#x_label#<br>#val#");

			for (PivotTableData.Axis category : categories) {
				PivotTableData.Cell cell = category.getCell(s);
				lineChart.addValues(cell == null ? 0 : cell.getValue());
			}
			cm.addChartConfig(lineChart);
		}
	}

	private void addPieChart(ChartModel cm,
			List<PivotTableData.Axis> categories, 
			List<PivotTableData.Axis> series) {

		PieChart pieChart = new PieChart();
		List<PieChart.Slice> slices = Lists.newArrayList();

		for (PivotTableData.Axis category : categories) {
			PivotTableData.Cell cell = category.getCell(series.get(0));
			if(cell != null) {
				slices.add(new PieChart.Slice(cell.getValue(), category.flattenLabel()));
			}
		}
		Collections.sort(slices, new Comparator<PieChart.Slice>() {

			@Override
			public int compare(Slice o1, Slice o2) {
				double d1 = o1.getValue().doubleValue();
				double d2 = o2.getValue().doubleValue();

				return Double.compare(d2, d1);
			}
		});
		pieChart.addSlices(slices);

		cm.addChartConfig(pieChart);
	}

}
