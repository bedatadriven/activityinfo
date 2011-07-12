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

	Indicator getIndicator();
	void setIndicator(Indicator indicator);

	/**
	 * Gets the list of indicators to map. The value at 
	 * each site used for scaling is equal to the sum
	 * of the values of the indicators in this list, or
	 * 1.0 if no indicators are specified.
	 */
	List<Integer> getIndicatorIds();
	void setIndicatorIds(List<Integer> indicatorIds);
	
	List<Indicator> getIndicators();
	void setIndicators(List<Indicator> indicators);
	
	public LabelSequence getLabelSequence();
	public void setLabelSequence(LabelSequence labelSequence);

	public boolean isClustered();
	public void setClustered(boolean clustered);
	
	public boolean supportsMultipleIndicators();
	
	public Clustering getClustering();
	public void setClustering(Clustering clustering);
}