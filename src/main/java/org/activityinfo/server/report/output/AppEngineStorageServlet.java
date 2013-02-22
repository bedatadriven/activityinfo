package org.activityinfo.server.report.output;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
import com.google.common.annotations.VisibleForTesting;

@Singleton
public class AppEngineStorageServlet extends HttpServlet {

	private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private final BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String keyName = parseBlobKey(req.getRequestURI());
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
		resp.setHeader("Content-Disposition", "attachment");
		blobstore.serve(blobKey, resp);
	}

	@VisibleForTesting
	static String parseBlobKey(String uri) {
		String prefix = "/generated/";
		int start = uri.indexOf(prefix);
		if(start == -1) {
			throw new IllegalArgumentException(uri);
		}
		start += prefix.length();
		int keyEnd = uri.indexOf('/', start+1);
		if(keyEnd == -1) {
			throw new IllegalArgumentException(uri);
		}
		return uri.substring(start, keyEnd);
	}

}
