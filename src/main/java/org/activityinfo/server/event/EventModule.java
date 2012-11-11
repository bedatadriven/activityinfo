package org.activityinfo.server.event;

import org.activityinfo.server.event.sitechange.SiteChangeListener;
import org.activityinfo.server.event.sitechange.SiteChangeServlet;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

public class EventModule extends ServletModule {

	@Override
	protected void configureServlets() {
		// eventbus
		bind(ServerEventBus.class).in(Singleton.class);
		
		// listeners
		bind(SiteChangeListener.class).in(Singleton.class);
		
		// define endpoints for async callbacks
		serve(SiteChangeServlet.ENDPOINT).with(SiteChangeServlet.class);
	}
}
