package org.activityinfo.shared.report.content;

import org.activityinfo.shared.report.content.PivotTableData;

import java.util.List;
import java.io.Serializable;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotChartContent extends PivotContent {

    private String yAxisTitle;
    private String xAxisTitle;

    public PivotChartContent() {
    }

    public String getYAxisTitle() {
        return yAxisTitle;
    }

    public void setYAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
    }

    public String getXAxisTitle() {
        return xAxisTitle;
    }

    public void setXAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
    }


    
}
