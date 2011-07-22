package org.sigmah.server.report.generator.map.cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sigmah.server.report.generator.map.RadiiCalculator;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.report.model.PointValue;
import org.sigmah.shared.report.model.clustering.AdministrativeLevelClustering;

/*
 * Clusters a set of points by the administrative level the points reside in, 
 * calculated per country
 */
public class AdminLevelClusterer implements Clusterer {
	private AdministrativeLevelClustering model;
	private RadiiCalculator radiiCalculator;
	private List<PointValue> points;
	
	public AdminLevelClusterer(
			AdministrativeLevelClustering adminLevelClustering,
			RadiiCalculator radiiCalculator,
			List<PointValue> points) {
		super();
		this.model = adminLevelClustering;
		this.points = points;
		this.radiiCalculator = radiiCalculator;
	}

	@Override
	public List<Cluster> cluster() {
		
		// admin entity id -> cluster
		Map<Integer, Cluster> adminClusters = new HashMap<Integer,Cluster>();
		
		for(PointValue pv : points) {
			int entityId = getAdminEntityId(pv);
			if(entityId != -1) {
				Cluster cluster = adminClusters.get(entityId);
				if(cluster == null) {
					cluster = new Cluster(pv);
					adminClusters.put(entityId, cluster);
				} else {
					cluster.addPointValue(pv);
				}
			}
		}
	
		ArrayList<Cluster> clusters = new ArrayList<Cluster>(adminClusters.values());
		radiiCalculator.calculate(clusters);
		
		return clusters;
	}

	private int getAdminEntityId(PointValue pv) {
		Map<Integer, AdminEntity> membership = pv.site.adminEntities;
		for(Integer levelId : model.getAdminLevels()) {
			if(membership.containsKey(levelId)) {
				return membership.get(levelId).getId();
			}
		}
		return -1;
	}
	
}
