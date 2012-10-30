package org.activityinfo.server.geo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Serves pre-encoded administrative unit geometry for the client. 
 *
 */
@Singleton
public class GeometryServlet extends HttpServlet {
	
	public static final String END_POINT = "/geometry/*";
	
	private GeometryStorage storage;
	
	@Inject
	public GeometryServlet(GeometryStorage storage) {
		super();
		this.storage = storage;
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if(req.getRequestURI().matches("/geometry/(\\d.*)")) {
			int levelId = Integer.parseInt(req.getRequestURI().substring("/geometry/".length()));
			boolean gzip = Strings.nullToEmpty(req.getHeader("Accept-Encoding")).contains("gzip");
			resp.setContentType("application/json");
			storage.serveJson(levelId, gzip, resp);
		} else {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	
}
