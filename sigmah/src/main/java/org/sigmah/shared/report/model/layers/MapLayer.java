package org.sigmah.shared.report.model.layers;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.sigmah.shared.domain.Indicator;
import org.sigmah.shared.report.model.clustering.Clustering;
import org.sigmah.shared.report.model.labeling.LabelSequence;

/*
 * A layer representing one or more indicators on a MapElement
 */
public interface MapLayer extends Serializable {

	boolean isVisible();
	void setVisible(boolean isVisible);

	/**
	 * Gets the list of indicators to map. The value at 
	 * each site used for scaling is equal to the sum
	 * of the values of the indicators in this list, or
	 * 1.0 if no indicators are specified.
	 */
	public List<Integer> getIndicatorIds();
	public void addIndicatorId(int Id);
	
	public LabelSequence getLabelSequence();
	public void setLabelSequence(LabelSequence labelSequence);

	public boolean isClustered();
	
	public boolean supportsMultipleIndicators();
	public boolean hasMultipleIndicators();
	
	public Clustering getClustering();
	public void setClustering(Clustering clustering);
	
	public String getInternationalizedName();
	
	/*
	 * Function to determine non-typesafe name of the class for gxt template usage
	 */
	public String getName();
}