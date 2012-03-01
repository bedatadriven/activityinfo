package org.activityinfo.login.server;

import com.google.inject.servlet.ServletModule;

public class LoginModule extends ServletModule {
	@Override
	protected void configureServlets() {
		serve("/Login/service").with(LoginServiceServlet.class);
	}
}
