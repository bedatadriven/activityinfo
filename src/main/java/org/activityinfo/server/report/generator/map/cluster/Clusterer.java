package org.activityinfo.server.report.generator.map.cluster;

import java.util.List;

import org.activityinfo.server.report.generator.map.TiledMap;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.model.PointValue;


public interface Clusterer {
	
	List<Cluster> cluster(TiledMap map, List<PointValue> points);
	boolean isMapped(SiteDTO site);
}
