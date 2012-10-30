package org.activityinfo.server.report.renderer.itext;

import java.io.File;

import org.activityinfo.server.geo.AdminGeometryProvider;
import org.activityinfo.server.geo.LocalGeometryStorage;
import org.activityinfo.server.geo.WkbGeometryProvider;

public abstract class TestGeometry {

	public static AdminGeometryProvider get() {
		//return new WkbGeometryProvider(new ClasspathGeometryProvider());
		//return new WkbGeometryProvider(new RemoteGcsStorage());
		return new WkbGeometryProvider(new LocalGeometryStorage(
				new File("C:/Users/Alex/aigeodb/geometry")));
	}
}
