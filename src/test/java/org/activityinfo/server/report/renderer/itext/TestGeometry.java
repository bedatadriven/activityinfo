package org.activityinfo.server.report.renderer.itext;

import org.activityinfo.server.geo.AdminGeometryProvider;
import org.activityinfo.server.geo.ClasspathGeometryProvider;
import org.activityinfo.server.geo.WkbGeometryProvider;

public abstract class TestGeometry {

	public static AdminGeometryProvider get() {
		return new WkbGeometryProvider(new ClasspathGeometryProvider());
	}
}
