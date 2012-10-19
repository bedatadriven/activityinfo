package org.activityinfo.server.report.renderer.geo;

import java.util.List;


public interface AdminGeometryProvider {

	List<AdminGeo> getGeometry(int adminLevelId);

}
