package org.activityinfo.shared.report.content;

import java.io.Serializable;
import java.util.List;

import org.activityinfo.shared.report.model.layers.PolygonMapLayer;


public class PolygonLegend extends MapLayerLegend<PolygonMapLayer>{

	public static class ColorClass implements Serializable {
		double minValue;
		double maxValue;
		String color;
		
		public ColorClass() {
			super();
		}
		
		public ColorClass(double minValue, double maxValue, String color) {
			super();
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.color = color;
		}
		public double getMinValue() {
			return minValue;
		}
		public void setMinValue(double minValue) {
			this.minValue = minValue;
		}
		public double getMaxValue() {
			return maxValue;
		}
		public void setMaxValue(double maxValue) {
			this.maxValue = maxValue;
		}
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
		
		
	}
	
	private List<ColorClass> classes;
	
	public PolygonLegend() {
		
	}

	public PolygonLegend(PolygonMapLayer layer, List<ColorClass> classes) {
		super(layer);
		this.classes = classes;
	}

	public List<ColorClass> getClasses() {
		return classes;
	}

	public void setClasses(List<ColorClass> classes) {
		this.classes = classes;
	}
	
	
	
}
