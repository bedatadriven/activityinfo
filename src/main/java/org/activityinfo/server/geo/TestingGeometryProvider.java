package org.activityinfo.server.geo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class TestingGeometryProvider extends WkbGeometryProvider {

	@Override
	protected InputStream openInput(int adminLevelId) throws IOException {
		InputStream in = getClass().getResourceAsStream("/" + adminLevelId + ".wkb");
		if(in == null) {
			throw new FileNotFoundException();
		}
		return in;
	}

}
