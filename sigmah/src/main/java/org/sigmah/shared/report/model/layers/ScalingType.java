package org.sigmah.shared.report.model.layers;

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