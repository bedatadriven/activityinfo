package org.activityinfo.server.endpoint.refine;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class RefineModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(ReconciliationService.class);
		bind(JacksonJsonProvider.class).in(Singleton.class);
		bind(RefineIndexTask.class);
		filter("/reconcile*").through(GuiceContainer.class);
		filter("/tasks/refine/index").through(GuiceContainer.class);
	}

}
