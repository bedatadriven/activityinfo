package org.activityinfo.server.geo;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

public interface GeometryStorage {

	InputStream openWkb(int adminLevelId) throws IOException;
	
	void serveJson(int adminLevelId, HttpServletResponse response) throws IOException;
	
}
