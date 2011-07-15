package org.sigmah.shared.report.model.clustering;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AdministrativeLevelClustering implements Clustering {

	@Override
	public boolean isClustered() {
		return true;
	}

}
