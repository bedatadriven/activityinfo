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
