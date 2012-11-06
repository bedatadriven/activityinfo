package org.activityinfo.shared.report.model.layers;

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
	private static List<String> colors = new ArrayList<String>();
	
	public PiechartMapLayer() {
	}
	
	// See also: http://cloford.com/resources/colours/500col.htm
	static {
		colors.add("4169E1"); // royal blue
		colors.add("EEEE00"); // yellow 2
		colors.add("EE0000"); // red 2
		colors.add("00EE00"); // green 2
		colors.add("912CEE"); // purple
		colors.add("7EC0EE"); // skyblue 2
		colors.add("EE9A00"); // orange 2
		colors.add("800000"); // maroon
		colors.add("B4EEB4"); // darkseagreen 2
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
	private String getNextColor() {
		// Use a modulo to prevent OutOfRangeException for indicator-happy users
		return colors.get(slices.size() % colors.size());
	}
	

	@Override
	public String toString() {
		return "PiechartMapLayer [slices=" + slices + "]";
	}

	public static class Slice implements Serializable {
		private String color;
		private int indicatorId;
		
		public Slice() {
		}

		public Slice(String color, int indicatorId) {
			super();
			this.color = color;
			this.indicatorId = indicatorId;
		}
		
		public String getColor() {
			return color;
		}
		
		public void setColor(String color) {
			this.color = color;
		}
		
		public int getIndicatorId() {
			return indicatorId;
		}
		
		public void setIndicatorId(int indicatorId) {
			this.indicatorId = indicatorId;
		}

		@Override
		public String toString() {
			return "Slice [color=" + color + ", indicatorId=" + indicatorId
					+ "]";
		}
		
		
	}
}
