package org.activityinfo.server.endpoint.crx;

import com.google.inject.servlet.ServletModule;

public class CrxModule extends ServletModule {

	@Override
	protected void configureServlets() {
		filter("*.crx").through(CrxFilter.class);
	}

}
