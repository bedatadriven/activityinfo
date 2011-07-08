package org.sigmah.shared.report.model.layers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/*
 * Displays multiple indicators using a piechart to visualize relative proportions
 */
public class PiechartMapLayer extends CircledMapLayer {
	private int reservedColorLastElement;
	private List<Integer> orderedColors = new ArrayList<Integer>();
	
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
	
}
