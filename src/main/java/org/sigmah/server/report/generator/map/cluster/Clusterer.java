package org.sigmah.server.report.generator.map.cluster;

import java.util.List;

import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.model.PointValue;
import org.sigmah.server.report.generator.map.TiledMap;


public interface Clusterer {
	
	List<Cluster> cluster(TiledMap map, List<PointValue> points);
	boolean isMapped(SiteDTO site);
}
