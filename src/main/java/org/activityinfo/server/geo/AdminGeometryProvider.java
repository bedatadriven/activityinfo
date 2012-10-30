package org.activityinfo.server.geo;

import java.util.List;


/**
 * Provides a set of geometry for the {@code AdminEntities} in a given
 * {@code AdminLevel}, used for server-side rendering. All geometry is provided
 * in the WGS84 geographic coordinate system.
 */
public interface AdminGeometryProvider {

	List<AdminGeo> getGeometry(int adminLevelId);

}
