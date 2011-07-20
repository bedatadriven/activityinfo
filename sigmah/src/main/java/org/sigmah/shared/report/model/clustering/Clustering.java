package org.sigmah.shared.report.model.clustering;

import java.io.Serializable;
import java.util.List;

import org.sigmah.server.report.ClusterImpl;
import org.sigmah.server.report.generator.map.MarkerGraph.IntersectionCalculator;
import org.sigmah.server.report.generator.map.RadiiCalculator;
import org.sigmah.shared.report.model.PointValue;

public interface Clustering extends Serializable {
	public boolean isClustered();
	public List<Cluster> cluster(Clusterer clusterer);
}
