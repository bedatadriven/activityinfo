package org.sigmah.shared.report.model.layers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/*
 * Displays multiple indicators using a piechart to visualize relative proportions
 */
public class PiechartMapLayer extends CircledMapLayer {
	private List<Slice> slices = new ArrayList<Slice>();

	@Override
	public boolean supportsMultipleIndicators() {
		return true;
	}
	
	@Override
	public List<Integer> getIndicatorIds() {
		List<Integer> result  = new ArrayList<Integer>();
		
		for (Slice slice : slices) {
			result.add(slice.getIndicatorId());
		}
		
		return result;
	}
	 
	//TODO: make jaxb
	public List<Slice> getSlices() {
		return slices;
	}

	@Override
	public String getName() {
		return "Piechart";
	}

	@Override
	public String getInternationalizedName() {
		return "Piechart";
	}
	
	@Override
	public void addIndicatorId(int Id) {
		slices.add(new Slice(-1, Id));
	}

	public static class Slice {
		private int color;
		private int indicatorId;
		
		public Slice(int color, int indicatorId) {
			super();
			this.color = color;
			this.indicatorId = indicatorId;
		}
		
		public int getColor() {
			return color;
		}
		
		public void setColor(int color) {
			this.color = color;
		}
		
		public int getIndicatorId() {
			return indicatorId;
		}
		
		public void setIndicatorId(int indicatorId) {
			this.indicatorId = indicatorId;
		}
	}
}
