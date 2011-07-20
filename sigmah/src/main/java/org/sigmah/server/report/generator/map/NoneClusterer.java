package org.sigmah.server.report.generator.map;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.server.report.ClusterImpl;
import org.sigmah.shared.report.model.PointValue;
import org.sigmah.shared.report.model.clustering.Cluster;
import org.sigmah.shared.report.model.clustering.Clusterer;

public class NoneClusterer implements Clusterer {
	private RadiiCalculator radiiCalculator;
	private List<PointValue> points;

	public NoneClusterer(RadiiCalculator radiiCalculator,
			List<PointValue> points) {
		this.radiiCalculator = radiiCalculator;
		this.points = points;
	}
	
	/*
	 * Maps each Point to a cluster, not performing an actual clustering algorithm whatsoever
	 * @see org.sigmah.shared.report.model.clustering.Clustering#cluster(java.util.List, org.sigmah.server.report.generator.map.RadiiCalculator)
	 */
	@Override
	public List<Cluster> cluster() {
        List<Cluster> clusters = new ArrayList<Cluster>();
        
        // No actaul clustering taking place, simply map each point to a new cluster
        for(PointValue point : points) {
            clusters.add(new ClusterImpl(point));
        }
        
        // Not sure if this thing belongs here
        radiiCalculator.calculate(clusters);

        return clusters;
	}
}