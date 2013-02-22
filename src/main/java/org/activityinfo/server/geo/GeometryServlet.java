package org.activityinfo.server.geo;

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
