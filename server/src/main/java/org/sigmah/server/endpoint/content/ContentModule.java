package org.sigmah.server.endpoint.content;

import com.google.inject.servlet.ServletModule;

/**
 * Serves content from the externally hosted CMS.
 */
public class ContentModule extends ServletModule {

	@Override
	protected void configureServlets() {
		serve("/content/*").with(ContentServlet.class);
	}
}
