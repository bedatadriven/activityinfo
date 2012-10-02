package org.activityinfo.server.endpoint.content;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;

@Singleton
public class ContentServlet extends HttpServlet {

	public static final String PREFIX = "/content";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Previously we used a reverse proxy to serve wordpress content 
		// from the same host
		// This directs old links to the new location
		String uri = req.getRequestURI().substring(PREFIX.length());
		resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		resp.setHeader("Location", "http://about.activityinfo.org" + uri);
	}

}
