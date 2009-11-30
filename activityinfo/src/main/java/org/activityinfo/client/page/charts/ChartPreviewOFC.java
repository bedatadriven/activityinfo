package org.activityinfo.client.page.charts;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.axis.XAxis;
import com.extjs.gxt.charts.client.model.axis.YAxis;
import com.extjs.gxt.charts.client.model.charts.FilledBarChart;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.activityinfo.client.Application;
import org.activityinfo.shared.report.content.PivotChartContent;
import org.activityinfo.shared.report.content.PivotTableData;

import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ChartPreviewOFC extends ContentPanel implements ChartPreview {

    private Chart chart;
    private ChartModel chartModel;

    public ChartPreviewOFC() {
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

    protected void createChart() {
        chart = new Chart(GWT.getModuleBaseURL() + "/gxt210/chart/open-flash-chart.swf");
        chart.setBorders(false);
        chart.setChartModel(chartModel);

        if(!chart.isLoaded()) {
            this.el().mask(Application.CONSTANTS.loading());
            chart.addListener(Events.Ready, new Listener<BaseEvent>() {
                public void handleEvent(BaseEvent be) {
                    chart.removeListener(Events.Ready, this);
                    ChartPreviewOFC.this.el().unmask();
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

    public void setContent(PivotChartContent content) {

        /*

        PIE CHART
        &title=Pie+Chart,{font-size:18px; color: #d01f3c}&
&x_axis_steps=1&
&y_ticks=5,10,5&
&line=3,#87421F&
&y_min=0&
&y_max=20&
&pie=60,#505050,{font-size: 12px; color: #404040;&
&values=5,11,13,10,10&
&pie_labels=IE,Firefox,Opera,Wii,Other&
&colours=#d01f3c,#356aa0,#C79810&
&links=&
&tool_tip=%23val%23%25&

&title=Bar+Chart,{font-size: 26px;}&
&x_label_style=10,#9933CC,0,2&
&x_axis_steps=2&
&y_legend=Open Flash Chart,12,0x736AFF&
&y_ticks=5,10,2&
&x_labels=January,February,March,April,May,June,July,August,September&
&y_min=0&
&y_max=10&
&bar=50,#0066CC,Me,10&
&values=4,2,4,5,2,3,5,2,3&
&bar_2=50,#9933CC,You,10&
&values_2=9,8,7,9,7,5,8,7,9&
&bar_3=50,#639F45,Them,10&
&values_3=2,7,7,7,6,2,9,3,6&



         */

        ChartModel cm = new ChartModel();
        cm.setBackgroundColour("#fffff");
        cm.setNumDecimals(0);

        PivotTableData table = content.getData();

        List<PivotTableData.Axis> categories = table.getRootRow().getLeaves();
        List<PivotTableData.Axis> series = table.getRootColumn().getLeaves();

        cm.setDecimalSeparatorComma(true);
        XAxis xa = new XAxis();
        xa.setLabels(PivotTableData.flattenLabels(categories));
        xa.getLabels().setColour("009900");
        xa.setGridColour("eeffee");
        xa.setColour("009900");
        cm.setXAxis(xa);

        YAxis ya = new YAxis();
        double maxValue = table.getRootRow().getMaxValue();
        ya.setRange(0, maxValue);
        ya.setSteps(maxValue / 10);
        ya.setGridColour("eeffee");
        ya.setColour("009900");
        cm.setYAxisLabelStyle(10, "009900");
        cm.setYAxis(ya);

        FilledBarChart bchart = new FilledBarChart("6666ff", "000066");
        bchart.setTooltip("#val#");
        for (PivotTableData.Axis category : categories) {

            PivotTableData.Cell cell = category.getCell(series.get(0));
            bchart.addValues(cell == null ? 0 : cell.getValue());
        }
        cm.addChartConfig(bchart);

        setModel(cm);
    }



}
