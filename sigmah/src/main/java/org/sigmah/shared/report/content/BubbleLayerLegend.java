package org.sigmah.shared.report.content;

import org.sigmah.shared.report.model.layers.BubbleMapLayer;

public class BubbleLayerLegend extends MapLayerLegend<BubbleMapLayer> {

    private double minValue = Double.MAX_VALUE;
    private double maxValue = -Double.MAX_VALUE;
    
		
	/**
	 *
	 * @return the indicator value that corresponds to the minimum-sized bubble
	 * 
	 */
	public double getMinValue() {
		return minValue;
	}
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	
	/**
	 * 
	 * @return the indicator value that corresponds to the maximum-sized bubble
	 */
	public double getMaxValue() {
		return maxValue;
	}
	
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
	
	public boolean hasValues() {
		return minValue <= maxValue;
	}
}
