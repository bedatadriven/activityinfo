package org.sigmah.shared.report.model.clustering;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.sigmah.server.report.ClusterImpl;


/*
 * Does not do any clustering on map markers. Instead it just maps a pointvalue to a cluster.
 */
@XmlRootElement
public class NoClustering implements Clustering {
	@Override
	public boolean isClustered() {
		return false;
	}

	@Override
	public List<Cluster> cluster(Clusterer clusterer) {
		return clusterer.cluster();
	}
	
}