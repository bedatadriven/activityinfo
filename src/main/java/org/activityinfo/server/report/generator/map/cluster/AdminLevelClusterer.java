package org.activityinfo.server.report.generator.map.cluster;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activityinfo.server.report.generator.map.RadiiCalculator;
import org.activityinfo.server.report.generator.map.TiledMap;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.Point;
import org.activityinfo.shared.report.model.PointValue;
import org.activityinfo.shared.report.model.clustering.AdministrativeLevelClustering;

import com.google.common.collect.Lists;

/**
 * Clusters a set of points by the administrative level the points reside in, 
 * calculated per country
 */
public class AdminLevelClusterer implements Clusterer {
	private AdministrativeLevelClustering model;
	private RadiiCalculator radiiCalculator;
	
	public AdminLevelClusterer(
			AdministrativeLevelClustering adminLevelClustering,
			RadiiCalculator radiiCalculator) {
		super();
		this.model = adminLevelClustering;
		this.radiiCalculator = radiiCalculator;
	}

	@Override
	public List<Cluster> cluster(TiledMap map, List<PointValue> points) {
		 
		// admin entity id -> cluster
		Map<Integer, Cluster> adminClusters = new HashMap<Integer,Cluster>();
		
		for(PointValue pv : points) {
			AdminEntityDTO entity = getAdminEntityId(pv);
			if(entity != null) {
				Cluster cluster = adminClusters.get(entity.getId());
				if(cluster == null) {
					cluster = new Cluster(pv);
					cluster.setPoint(adminCenter(map, entity));
					cluster.setTitle(entity.getName());
					adminClusters.put(entity.getId(), cluster);
				} else {
					cluster.addPointValue(pv);
				}
			}
		}
	
		ArrayList<Cluster> clusters = Lists.newArrayList();
		
		// update centers of clusters based on points, if any
		for(Cluster cluster : adminClusters.values()) {
			updateCenter(cluster);
			if(cluster.hasPoint()) {
				clusters.add(cluster);
			}
		}
		radiiCalculator.calculate(clusters);
		
		return clusters;
	}

	private void updateCenter(Cluster cluster) {
		double count = 0;
		double sumX = 0;
		double sumY = 0;
		
		for(PointValue pv : cluster.getPointValues()) {
			if(pv.hasPoint()) {
				count ++;
				sumX += pv.getPoint().getDoubleX();
				sumY += pv.getPoint().getDoubleY();
			}
		}
		
		if(count > 0) {
			cluster.setPoint(new Point(sumX / count, sumY / count));
		}
	}

	private Point adminCenter(TiledMap map, AdminEntityDTO entity) {
		if(entity.getBounds() == null) {
			return null;
		} else {
			AiLatLng center = new AiLatLng( 
					(entity.getBounds().getMinLat() + entity.getBounds().getMaxLat()) / 2d,
					(entity.getBounds().getMinLon() + entity.getBounds().getMaxLon()) / 2d);
					
			return map.fromLatLngToPixel(center);
		}
	}

	private AdminEntityDTO getAdminEntityId(PointValue pv) {
		return getAdminEntity(pv.getSite());
	}

	private AdminEntityDTO getAdminEntity(SiteDTO site) {
		Map<Integer, AdminEntityDTO> membership = site.getAdminEntities();
		for(Integer levelId : model.getAdminLevels()) {
			if(membership.containsKey(levelId)) {
				return membership.get(levelId);
			}
		}
		return null;
	}

	@Override
	public boolean isMapped(SiteDTO site) {
		return getAdminEntity(site) != null;
	}
}
