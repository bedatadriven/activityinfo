package org.activityinfo.server.report.output;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Singleton
public class AppEngineStorageServlet extends HttpServlet {

	private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private final BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String uri = req.getRequestURI();
		int lastSlash = uri.lastIndexOf('/');
		String keyName = uri.substring(lastSlash+1);
		Key key = KeyFactory.createKey("TempFile", keyName);
		
		Entity entity;
		try {
			entity = datastore.get(key);
		} catch (EntityNotFoundException e) {
			resp.sendError(404);
			return;
		}
		BlobKey blobKey = (BlobKey) entity.getProperty("blobKey");
		
		resp.setContentType((String)entity.getProperty("mimeType"));
		resp.setHeader("Content-Disposition", "attachment; filename=\"activityinfo\"");
		blobstore.serve(blobKey, resp);
	}

}
