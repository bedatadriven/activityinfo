package org.activityinfo.server.geo;

import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

public class ClasspathGeometryProvider implements GeometryStorage {


	@Override
	public InputStream openWkb(int adminLevelId) {
		InputStream in = getClass().getResourceAsStream("/" + adminLevelId + ".wkb");
		if(in == null) {
			throw new RuntimeException("No wkb geometry for level " + adminLevelId + " on classpath");
		}
		return in;
	}

	@Override
	public void serveJson(int adminLevelId, boolean gzip, HttpServletResponse response) {
		throw new UnsupportedOperationException();
	}

}
