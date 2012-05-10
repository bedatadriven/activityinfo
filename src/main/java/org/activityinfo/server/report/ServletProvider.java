package org.activityinfo.server.report;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Provider;


public class ServletProvider implements Provider<HttpServletRequest> {
	@Override
	public HttpServletRequest get() {
		return null;
	}
}
