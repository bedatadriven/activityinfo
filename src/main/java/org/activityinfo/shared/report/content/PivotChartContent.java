package org.activityinfo.shared.report.content;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

/**
 * @author Alex Bertram
 */
public class PivotChartContent extends PivotContent {

    private String yAxisTitle;
    private String xAxisTitle;
    private double yMin;
    private double yStep = 1.0;

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
     * @return the step value for the y-Axis
     */
    public double getYStep() {
        return yStep;
    }

    public void setYStep(double yStep) {
        if (yStep > 0.0) {
            this.yStep = yStep;
        }
    }
}
