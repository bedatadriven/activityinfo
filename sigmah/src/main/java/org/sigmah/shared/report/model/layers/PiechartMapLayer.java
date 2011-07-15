package org.sigmah.shared.report.model.layers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/*
 * Displays multiple indicators using a piechart to visualize relative proportions
 */
public class PiechartMapLayer extends CircledMapLayer {
	private List<Slice> slices = new ArrayList<Slice>();
	private static List<Integer> colors = new ArrayList<Integer>();
	
	public PiechartMapLayer() {
	}
	
	static {
		colors.add(16711680); // red
		colors.add(16776960); // yellow
		colors.add(16777215); // white
		colors.add(255);    // darkblue
		colors.add(65280); // green
		colors.add(8388736); // purple
		colors.add(16711680); // red
		colors.add(16776960); // yellow
		colors.add(16777215); // white
		colors.add(255);    // darkblue
		colors.add(65280); // green
		colors.add(8388736); // purple
		colors.add(16711680); // red
		colors.add(16776960); // yellow
		colors.add(16777215); // white
		colors.add(255);    // darkblue
		colors.add(65280); // green
		colors.add(8388736); // purple
		colors.add(16711680); // red
		colors.add(16776960); // yellow
		colors.add(16777215); // white
		colors.add(255);    // darkblue
		colors.add(65280); // green
		colors.add(8388736); // purple
	}

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
	
	@XmlElement(name = "slice")
	@XmlElementWrapper(name = "slices")
	public List<Slice> getSlices() {
		return slices;
	}

	@Override
	public String getTypeName() {
		return "Piechart";
	}
	
	@Override
	public void addIndicatorId(int Id) {
		slices.add(new Slice(getNextColor(), Id));
	}
	
	/*
	 * Returns next color for a slice
	 */
	private int getNextColor() {
		return colors.get(slices.size());
	}

	public static class Slice implements Serializable {
		private int color;
		private int indicatorId;
		
		public Slice() {
		}

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
