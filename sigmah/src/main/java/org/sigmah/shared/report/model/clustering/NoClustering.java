package org.sigmah.shared.report.model.clustering;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NoClustering implements Clustering {
	@Override
	public boolean isClustered() {
		return false;
	}

}
