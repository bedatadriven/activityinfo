/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.content;

/**
 * @author Alex Bertram
 */
public class PivotChartContent extends PivotContent {

    private String yAxisTitle;
    private String xAxisTitle;
    private double yMin;
    private double yMax;
    private double yStep;

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

    /**
     * Gets the minimum value for the y-Axis
     *
     * @return the minimum value for the y-Axis
     */
    public double getYMin() {
        return yMin;
    }

    public void setYMin(double yMin) {
        this.yMin = yMin;
    }

    /**
     * Gets the step value for the y-Axis (major unit)
     *
     * @return  the step value for the y-Axis
     */
    public double getYStep() {
        return yStep;
    }

    public void setYStep(double yStep) {
        this.yStep = yStep;
    }
}
