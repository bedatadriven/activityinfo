package org.sigmah.shared.report.model.clustering;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.sigmah.server.report.ClusterImpl;


@XmlRootElement
public class AutomaticClustering implements Clustering { 

	@Override
	public boolean isClustered() { 
		return true;
	}

	@Override
	public List<Cluster> cluster(Clusterer clusterer) {
		return clusterer.cluster();
	}
}