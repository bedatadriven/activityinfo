package org.activityinfo.shared.report.model.layers;

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

public enum ScalingType {
    /**
     * The symbols will not scaled according to the
     * indicator value; all symbols will have a radius of
     * <code>minRadius</code>
     */
    None,
    /**
     * The symbols will be scaled evenly between
     * <code>minRadius</code> and <code>maxRadius</code>
     */
    Graduated,

    /**
     * The symbols will be scaled proportionately.
     * The scale factor is caculated based on the
     * minimum plotted value and <code>minRadius</code>
     *
     * If <code>maxRadius</code> is greater than 0, incremental
     * logarithmic scaling will be applied iteratively
     * until the maximum plotted radius is less than or
     * equal to <code>maxRadius</code>
     */
    Proportional
}