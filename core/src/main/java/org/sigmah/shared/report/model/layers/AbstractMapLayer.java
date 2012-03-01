/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model.layers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import org.sigmah.shared.command.Filter;
import org.sigmah.shared.report.model.clustering.AdministrativeLevelClustering;
import org.sigmah.shared.report.model.clustering.AutomaticClustering;
import org.sigmah.shared.report.model.clustering.Clustering;
import org.sigmah.shared.report.model.clustering.NoClustering;
import org.sigmah.shared.report.model.labeling.ArabicNumberSequence;
import org.sigmah.shared.report.model.labeling.LabelSequence;
import org.sigmah.shared.report.model.labeling.LatinAlphaSequence;

/*
 * Convenience implementation for MapLayer implementors
 */
public abstract class AbstractMapLayer implements MapLayer {
	private boolean isVisible = true;
	private List<Integer> indicatorIds = new ArrayList<Integer>();
	private LabelSequence labelSequence = new LatinAlphaSequence();
	private Clustering clustering =  new NoClustering(); 
	private String name;
	private Filter filter = new Filter();

	@Override
	public void addIndicatorId(int id) {
	    indicatorIds.add(id);
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	@Override
	@XmlElement(name = "indicator")
	@XmlElementWrapper(name = "indicators")
	public List<Integer> getIndicatorIds() {
	    return indicatorIds;
	}
	
	@Override
	@XmlElementRefs({
		@XmlElementRef(type=ArabicNumberSequence.class),
		@XmlElementRef(type=LatinAlphaSequence.class)
		})
	public LabelSequence getLabelSequence() {
		return labelSequence;
	}

	@Override
	public void setLabelSequence(LabelSequence labelSequence) {
		this.labelSequence = labelSequence;
	}

	@Override
	@XmlTransient
	public boolean isClustered() {
	    return clustering.isClustered();
	}

	@Override
	@XmlElementRefs({
		@XmlElementRef(type=AdministrativeLevelClustering.class),
		@XmlElementRef(type=NoClustering.class),
		@XmlElementRef(type=AutomaticClustering.class)
		})
	public Clustering getClustering() {
		return clustering;
	}

	@Override
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

	@Override
	public Filter getFilter() {
		return filter;
	}

	@Override
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
	
}
