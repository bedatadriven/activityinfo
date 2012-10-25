package org.activityinfo.server.geo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import com.google.common.io.Resources;


public class RemoteGcsStorage implements GeometryStorage {

	@Override
	public InputStream openWkb(int adminLevelId) throws IOException {
		URL url = new URL("http://commondatastorage.googleapis.com/aigeo/" + adminLevelId  + ".wkb");
		return url.openStream();
	}

	@Override
	public void serveJson(int adminLevelId, HttpServletResponse response)
			throws IOException {
		URL url = new URL("http://commondatastorage.googleapis.com/aigeo/" + adminLevelId  + ".json.gz");
		response.setHeader("Content-Encoding", "gzip");
		Resources.copy(url, response.getOutputStream());
	}

}
