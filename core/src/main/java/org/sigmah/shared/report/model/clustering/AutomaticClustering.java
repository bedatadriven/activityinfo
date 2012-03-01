package org.sigmah.shared.report.model.clustering;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class AutomaticClustering implements Clustering { 

	@Override
	public boolean isClustered() { 
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.getClass().equals(getClass());
	}

	@Override
	public int hashCode() {
		return 0;
	}


}