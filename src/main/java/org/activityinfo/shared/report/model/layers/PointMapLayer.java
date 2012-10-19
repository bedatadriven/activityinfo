package org.activityinfo.shared.report.model.layers;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlTransient;

import org.activityinfo.shared.report.model.clustering.AdministrativeLevelClustering;
import org.activityinfo.shared.report.model.clustering.AutomaticClustering;
import org.activityinfo.shared.report.model.clustering.Clustering;
import org.activityinfo.shared.report.model.clustering.NoClustering;
import org.activityinfo.shared.report.model.labeling.ArabicNumberSequence;
import org.activityinfo.shared.report.model.labeling.LabelSequence;
import org.activityinfo.shared.report.model.labeling.LatinAlphaSequence;

public abstract class PointMapLayer extends AbstractMapLayer {

	private LabelSequence labelSequence;
	private Clustering clustering = new NoClustering();
	

	@XmlElementRefs({ @XmlElementRef(type = ArabicNumberSequence.class),
			@XmlElementRef(type = LatinAlphaSequence.class) })
	public LabelSequence getLabelSequence() {
		return labelSequence;
	}
	
	public void setLabelSequence(LabelSequence labelSequence) {
		this.labelSequence = labelSequence;
	}

	@XmlTransient
	public boolean isClustered() {
	    return clustering.isClustered();
	}

	@XmlElementRefs({
			@XmlElementRef(type = AdministrativeLevelClustering.class),
			@XmlElementRef(type = NoClustering.class),
			@XmlElementRef(type = AutomaticClustering.class) })
	public Clustering getClustering() {
		return clustering;
	}

	public void setClustering(Clustering clustering) {
		this.clustering = clustering;
	}

}
