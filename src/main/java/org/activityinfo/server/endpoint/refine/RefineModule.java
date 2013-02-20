package org.activityinfo.server.endpoint.refine;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class RefineModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(ReconciliationService.class);
		bind(JacksonJsonProvider.class).toInstance(new JacksonJsonProvider(createObjectMapper()));
		bind(RefineIndexTask.class);
		filter("/reconcile*").through(GuiceContainer.class);
		filter("/tasks/refine/index").through(GuiceContainer.class);
	}

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JsonDtoModule("dto", Version.unknownVersion()));
        return mapper;
    }

}
