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