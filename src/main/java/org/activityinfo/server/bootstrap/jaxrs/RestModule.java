package org.activityinfo.server.bootstrap.jaxrs;

import javax.ws.rs.Path;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public abstract class RestModule extends ServletModule {

	@Override
	protected final void configureServlets() {
		configureResources();
	}

	protected abstract void configureResources();
	
	protected final void bindResource(Class clazz) {
		bind(clazz);

		Path path = (Path) clazz.getAnnotation(Path.class);
		if(path == null) {
			throw new IllegalStateException(clazz.getName() + " must have @Path annotation");
		}
		filter(path.value() + "*").through(GuiceContainer.class);
	}

	protected final void bindResource(Class clazz, String pattern, String... morePatterns) {
		bind(clazz);
		filter(pattern, morePatterns).through(GuiceContainer.class);
	}
	
}
