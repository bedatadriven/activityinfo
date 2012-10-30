package org.activityinfo.server.report.generator.map;

import org.activityinfo.shared.map.RgbColor;


public class MagnitudeScale {

	private double maxValue = 0;
	private RgbColor maxColor;
	private RgbColor minColor = new RgbColor(255,255,255);
	
	public MagnitudeScale() {
		maxColor = new RgbColor(255,0,0);
	}
	
	public MagnitudeScale(String color) {
		this.maxColor = new RgbColor(color);
	}
	
	public void addValue(double value) {
		if(value > maxValue) {
			maxValue = value;
		}
	}
	
	public RgbColor nullColor() {
		return minColor;
	}
	
	public RgbColor color(double value) {
		if(value <= 0) {
			return minColor;
		} else {
			return RgbColor.interpolate(minColor, maxColor, value / maxValue);
		}
	}
	
}
