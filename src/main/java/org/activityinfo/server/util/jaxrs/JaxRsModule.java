package org.activityinfo.server.util.jaxrs;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.google.inject.servlet.ServletModule;

/**
 * Provides the basic configuration for the Jersey Container,
 * including the FreeMarker ViewProcessor and the 
 * Jackson Json Provider. 
 *
 */
public class JaxRsModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bind(FreemarkerViewProcessor.class);
        bind(JacksonJsonProvider.class).toInstance(new Utf8JacksonJsonProvider());
    }
}
