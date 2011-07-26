package org.sigmah.server.report.generator.map.cluster;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.server.report.generator.map.RadiiCalculator;
import org.sigmah.shared.report.model.PointValue;

/**
 * Does not cluster at all, rather maps points 1:1 to clusters
 */
public class NullClusterer implements Clusterer {
	private RadiiCalculator radiiCalculator;
	private List<PointValue> points;

	public NullClusterer(RadiiCalculator radiiCalculator,
			List<PointValue> points) {
		this.radiiCalculator = radiiCalculator;
		this.points = points;
	}
	
	/**
	 * Maps each Point to a cluster, not performing an actual clued.report.model.clustering.Clusteristering algorithm whatsoever
	 * @see org.sigmah.sharng#cluster(java.util.List, org.sigmah.server.report.generator.map.RadiiCalculator)
	 */
	@Override
	public List<Cluster> cluster() {
        List<Cluster> clusters = new ArrayList<Cluster>();
        
        // No actaul clustering taking place, simply map each point to a new cluster
        for(PointValue point : points) {
            clusters.add(new Cluster(point));
        }
        
        // Not sure if this thing belongs here
        radiiCalculator.calculate(clusters);

        return clusters;
	}
}