package org.sigmah.server.report.generator.map.cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.server.report.ClusterImpl;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.report.model.PointValue;
import org.sigmah.shared.report.model.clustering.AdministrativeLevelClustering;
import org.sigmah.shared.report.model.clustering.Cluster;
import org.sigmah.shared.report.model.clustering.Clusterer;

/*
 * Clusters a set of points by the administrative level the points reside in, 
 * calculated per country
 */
public class AdminLevelClusterer implements Clusterer {
	private AdministrativeLevelClustering adminLevelClustering;
	private List<PointValue> points;
	private Map<AdminLevelDTO, List<PointValue>> pointsByAdminLevel = new HashMap<AdminLevelDTO, List<PointValue>>();
	private List<Cluster> pointsClusteredByAdminLevel = new ArrayList<Cluster>();
	public AdminLevelClusterer(
			AdministrativeLevelClustering adminLevelClustering,
			List<PointValue> points) {
		super();
		this.adminLevelClustering = adminLevelClustering;
		this.points = points;
	}

	@Override
	public List<Cluster> cluster() {
		
		sortPointsByAdminLevel();
		convertMapToClusterList();
		
		return pointsClusteredByAdminLevel;
	}

	private void convertMapToClusterList() {
		for (List<PointValue> points : pointsByAdminLevel.values()) {
			Cluster cluster = new ClusterImpl(points);
			pointsClusteredByAdminLevel.add(cluster);
		}
	}

	private void sortPointsByAdminLevel() {
		// TODO: implement me
	}
	
	private void addToMap(AdminLevelDTO adminLevel, PointValue point) {
		if (!pointsByAdminLevel.containsKey(adminLevel)) {
			pointsByAdminLevel.put(adminLevel, new ArrayList<PointValue>());
		}
		
		pointsByAdminLevel.get(adminLevel).add(point);
	}

}
