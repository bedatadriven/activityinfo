package org.sigmah.shared.report.model.clustering;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sigmah.server.report.ClusterImpl;
import org.sigmah.server.report.generator.map.MarkerGraph.IntersectionCalculator;
import org.sigmah.server.report.generator.map.RadiiCalculator;
import org.sigmah.shared.report.model.PointValue;

@XmlRootElement
public class AdministrativeLevelClustering implements Clustering {
	private List<Integer> adminLevels = new ArrayList<Integer>();
	private NoClustering noClustering = new NoClustering();
	
	@XmlElement
	public List<Integer> getAdminLevels() {
		return adminLevels;
	}
	
	@Override
	public boolean isClustered() {
		return true;
	}

	@Override
	public List<Cluster> cluster(Clusterer clusterer) {
		return clusterer.cluster();
	}
}