package org.activityinfo.server.report.generator.map;

import java.util.List;

import org.activityinfo.shared.map.RgbColor;
import org.activityinfo.shared.report.content.PolygonLegend;
import org.activityinfo.shared.report.content.PolygonLegend.ColorClass;
import org.activityinfo.shared.report.model.layers.PolygonMapLayer;

import com.google.common.collect.Lists;


public class MagnitudeScaleBuilder {

	private RgbColor maxColor;
	private RgbColor minColor = new RgbColor(255,255,255);
	private Jenks jenks = new Jenks();
	private PolygonMapLayer layer;
	
	
	public MagnitudeScaleBuilder(PolygonMapLayer layer) {
		this.layer = layer;
		this.maxColor = new RgbColor(layer.getMaxColor());
	}
	
	public void addValue(double value) {
		if(value > 0) {
			jenks.addValue(value);
		}
	}
	
	public RgbColor nullColor() {
		return minColor;
	}
	

	public Scale build() {
		return new Scale();
	}
	
	public class Scale {
		
		private Jenks.Breaks breaks;

		private Scale() {
			breaks = jenks.computeBreaks();
		}
		
		public RgbColor color(Double value) {
			if(value == null || value <= 0) {
				return minColor;
			} else {
				// we add an extra category for 0/null
				double p = ((double)(breaks.classOf(value)+1)) / ((double)(breaks.numClassses()+1));
				return RgbColor.interpolate(minColor, maxColor, p);
			}
		}
		
		public PolygonLegend legend() {
			List<PolygonLegend.ColorClass> classes = Lists.newArrayList();
			for(int i=0;i!=breaks.numClassses();++i) {
				PolygonLegend.ColorClass clazz = new ColorClass();
				clazz.setMinValue(breaks.getClassMin(i));
				clazz.setMaxValue(breaks.getClassMax(i));
				clazz.setColor(color(breaks.getClassMin(i)).toHexString());
				classes.add(clazz);
			}
			return new PolygonLegend(layer, classes);
			
		}
	}


}
