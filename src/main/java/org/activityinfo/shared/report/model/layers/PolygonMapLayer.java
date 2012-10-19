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

public class PolgonMapLayer extends AbstractMapLayer {

	@Override
	public boolean supportsMultipleIndicators() {
		return true;
	}

	@Override
	public String getTypeName() {
		return "Polygon";
	}


}
