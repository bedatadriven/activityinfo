package org.activityinfo.server.report.generator.map.cluster;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.server.report.generator.map.RadiiCalculator;
import org.activityinfo.server.report.generator.map.TiledMap;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.model.PointValue;

/**
 * Does not cluster at all, rather maps points 1:1 to clusters
 */
public class NullClusterer implements Clusterer {
	private RadiiCalculator radiiCalculator;

	public NullClusterer(RadiiCalculator radiiCalculator) {
		this.radiiCalculator = radiiCalculator;
	}
	
	/**
	 * Maps each Point to a cluster, not performing an actual clued.report.model.clustering.Clusteristering algorithm whatsoever
	 * @see org.activityinfo.sharng#cluster(java.util.List, org.activityinfo.server.report.generator.map.RadiiCalculator)
	 */
	@Override
	public List<Cluster> cluster(TiledMap map, List<PointValue> points) {
        List<Cluster> clusters = new ArrayList<Cluster>();
        
        // No actual clustering taking place, simply map each point to a new cluster
        for(PointValue point : points) {
            clusters.add(new Cluster(point));
        }
        
        radiiCalculator.calculate(clusters);
        return clusters;
	}

	@Override
	public boolean isMapped(SiteDTO site) {
		return site.hasLatLong();
	}
}