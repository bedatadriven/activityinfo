package org.activityinfo.shared.report.model;

import java.util.List;
import java.util.ArrayList;
/*
 * @author Alex Bertram
 */

public class GsMapLayer extends MapLayer {

    private List<Integer> indicatorIds = new ArrayList<Integer>();
    private List<Dimension> colorDimensions = new ArrayList<Dimension>();


    private int minRadius = 5;
    private int maxRadius = 30;
    private boolean clustered = true;
    private int defaultColor = 255; // blue
    private double alpha = 0.75;

    public GsMapLayer() {
    }

    public List<Integer> getIndicatorIds() {
        return indicatorIds;
    }

    public void setIndicatorIds(List<Integer> indicatorIds) {
        this.indicatorIds = indicatorIds;
    }

    public int getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(int minRadius) {
        this.minRadius = minRadius;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }


    public void addIndicator(int id) {
        indicatorIds.add(id);
    }

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

    public boolean isClustered() {
        return clustered;
    }

    public void setClustered(boolean clustered) {
        this.clustered = clustered;
    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
}
