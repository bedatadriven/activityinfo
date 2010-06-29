/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Defines a graduated symbol layer
 *
 * @author Alex Bertram
 */
public class BubbleMapLayer extends MapLayer {

    public enum NumberingType {

        /**
         * Symbols will not be numbered
         */
        None,

        /**
         * Bubbles will be labeled 1, 2, 3 ...
         */
        ArabicNumerals,

        /**
         * Bubbles will be labed A...Z, AA..AZ, etc
         */
        LatinAlphabet
    }

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

    private List<Integer> indicatorIds = new ArrayList<Integer>();
    private List<Dimension> colorDimensions = new ArrayList<Dimension>();

    private int minRadius = 5;
    private int maxRadius = 30;

    private boolean clustered = true;
    private int defaultColor = 255; // blue
    private double alpha = 0.75;

    private ScalingType scaling = ScalingType.None;
    private NumberingType numbering = NumberingType.None;
    private int labelColor;
    private boolean pie;

    public BubbleMapLayer() {
    }

    /**
     * Gets the list of indicators to map. The value at 
     * each site used for scaling is equal to the sum
     * of the values of the indicators in this list, or
     * 1.0 if no indicators are specified.
     *
     * @return  The list of indicators to map
     */
    @XmlElement(name="indicator")
    @XmlElementWrapper(name="indicators")
    public List<Integer> getIndicatorIds() {
        return indicatorIds;
    }

    public void setIndicatorIds(List<Integer> indicatorIds) {
        this.indicatorIds = indicatorIds;
    }

    @XmlElement
    public int getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(int minRadius) {
        this.minRadius = minRadius;
    }

    @XmlElement
    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }


    public void addIndicator(int id) {
        indicatorIds.add(id);
    }

    @XmlElement(name="dimension")
    @XmlElementWrapper(name="colors")
    public List<Dimension> getColorDimensions() {
        return colorDimensions;
    }

    public void setColorDimensions(List<Dimension> colorDimensions) {
        this.colorDimensions = colorDimensions;
    }

    public boolean containsIndicatorDimension() {
        for(Dimension dim : colorDimensions) {
            if(dim.getType() == DimensionType.Indicator) {
                return true;
            }
        }
        return false;
    }

    @XmlElement
    public boolean isClustered() {
        return clustered;
    }

    public void setClustered(boolean clustered) {
        this.clustered = clustered;
    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    @XmlElement
    public int getDefaultColor() {
        return defaultColor;
    }

    @XmlElement
    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    @XmlElement(defaultValue = "none")
    public ScalingType getScaling() {
        return scaling;
    }

    public void setScaling(ScalingType scaling) {
        this.scaling = scaling;
    }

    @XmlElement(defaultValue = "none")
    public NumberingType getNumbering() {
        return numbering;
    }

    public void setNumbering(NumberingType numbering) {
        this.numbering = numbering;
    }

    @XmlElement
    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    public boolean isPie() {
        return pie;
    }

    public void setPie(boolean pie) {
        this.pie = pie;
    }
}
