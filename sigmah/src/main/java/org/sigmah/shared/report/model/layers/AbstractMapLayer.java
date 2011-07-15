/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model.layers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.sigmah.shared.domain.Indicator;
import org.sigmah.shared.report.model.clustering.Clustering;
import org.sigmah.shared.report.model.clustering.NoClustering;
import org.sigmah.shared.report.model.labeling.LabelSequence;
import org.sigmah.shared.report.model.labeling.LatinAlphaSequence;

/*
 * Convenience implementation for MapLayer implementors
 */
public abstract class AbstractMapLayer implements MapLayer {
	private boolean isVisible = true;
	protected List<Integer> indicatorIds = new ArrayList<Integer>();
	protected LabelSequence labelSequence = new LatinAlphaSequence();
	protected Clustering clustering =  new NoClustering(); 
	protected String name;

	@Override
	public void addIndicatorId(int id) {
	    indicatorIds.add(id);
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	@XmlElement(name = "indicator")
	@XmlElementWrapper(name = "indicators")
	public List<Integer> getIndicatorIds() {
	    return indicatorIds;
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
	    return clustering.isClustered();
	}

	@XmlElement
	public Clustering getClustering() {
		return clustering;
	}

	public void setClustering(Clustering clustering) {
		this.clustering = clustering;
	}

	/*
	 * Returns true when there is a list of indicatorIDs and the amount of indicatorIDs is more then one
	 */
	@Override
	public boolean hasMultipleIndicators() {
		return indicatorIds != null && indicatorIds.size() > 1;
	}

	@XmlElement
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name=name;
	}
}
