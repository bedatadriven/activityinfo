/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model.layers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.sigmah.shared.domain.Indicator;
import org.sigmah.shared.report.model.labeling.LabelSequence;
import org.sigmah.shared.report.model.labeling.LatinAlphaSequence;

/*
 * Convenience implementation for MapLayer implementors
 */
public abstract class AbstractMapLayer implements MapLayer {

	private boolean isVisible = true;
	private Indicator indicator;
	protected List<Integer> indicatorIds = new ArrayList<Integer>();
	protected LabelSequence labelSequence = new LatinAlphaSequence();
	private boolean clustered = true;

	public void addIndicator(int id) {
	    indicatorIds.add(id);
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	@XmlElement(name = "indicator")
	@XmlElementWrapper(name = "indicators")
	public List<Integer> getIndicatorIds() {
	    return indicatorIds;
	}

	public void setIndicatorIds(List<Integer> indicatorIds) {
	    this.indicatorIds = indicatorIds;
	}

	@XmlElement(type=Object.class)
	public LabelSequence getLabelSequence() {
		return labelSequence;
	}

	public void setLabelSequence(LabelSequence labelSequence) {
		this.labelSequence = labelSequence;
	}

	@XmlElement
	public boolean isClustered() {
	    return clustered;
	}

	public void setClustered(boolean clustered) {
	    this.clustered = clustered;
	}
}
