package org.activityinfo.server.geo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

/**
 * Serves pre-encoded administrative unit geometry for the client. 
 *
 */
public class GeometryServlet extends HttpServlet {
	
	public static final String END_POINT = "/geometry/*";
	
	private BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if(req.getRequestURI().matches("/geometry/(\\d.*)")) {
			int levelId = Integer.parseInt(req.getRequestURI().substring("/geometry/".length()));
			BlobKey blobKey = blobstore.createGsBlobKey("/gs/aigeo/" + levelId + ".json");
			resp.setContentType("application/json");
			blobstore.serve(blobKey, resp);
		} else {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
