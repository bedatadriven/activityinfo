package org.sigmah.shared.report.model.layers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.sigmah.shared.report.content.PieMapMarker.Slice;

/*
 * Displays multiple indicators using a piechart to visualize relative proportions
 */
public class PiechartMapLayer extends CircledMapLayer {
	private List<Slice> slices = new ArrayList<Slice>();
	
	/*
	 * Returns a color based on the position of the indicator.
	 * 
	 * Only a fixed list of colors is used. These colors are based on alternate contrasts.
	 * Alternate contrasted colors mean subsequent indicators can be distinguished clearly, even
	 * on photocopied paper.
	 */
	@XmlElement
	public int getColor(int indicatorId) {
		//TODO: implement static list of colors
		//TODO: make sure the first color and last color always contrast
		// 
		
		return this.indicatorIds.indexOf(indicatorId) * 65000;
	}

	@Override
	public boolean supportsMultipleIndicators() {
		return true;
	}
	
	public List<Slice> getSlices() {
		return slices;
	}
}
