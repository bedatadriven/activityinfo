package org.sigmah.server.report.generator.map.cluster;

import java.util.List;

import org.sigmah.server.report.generator.map.TiledMap;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.report.model.PointValue;


public interface Clusterer {
	
	List<Cluster> cluster(TiledMap map, List<PointValue> points);
	boolean isMapped(SiteDTO site);
}
