package org.activityinfo.server.util.jaxrs;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.ws.rs.Path;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Base class for Guice Modules that provide REST endpoints. 
 * Use the bindResource() shortcut to tell the GuiceContainer
 * about your resource and add it's @Path to the list of uris
 * that are sent to GuiceContainer.
 */
public abstract class AbstractRestModule extends ServletModule {
    protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    @Override
    protected final void configureServlets() {
        configureResources();
    }

    protected abstract void configureResources();

    protected final void bindResource(Class clazz) {
        bind(clazz);

        Path path = (Path) clazz.getAnnotation(Path.class);
        if (path == null) {
            throw new IllegalStateException(clazz.getName() + " must have @Path annotation");
        }
        String pattern = path.value();

        // only add the wildcard to the url pattern if the class contains at least
        // one method with the Path annotation
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(Path.class)) {
                pattern += "*";
                break;
            }
        }

        LOGGER.info("binding REST path '" + pattern + "'");
        filter(pattern).through(GuiceContainer.class);
    }

    protected final void bindResource(Class clazz, String pattern,
        String... morePatterns) {
        bind(clazz);
        filter(pattern, morePatterns).through(GuiceContainer.class);
    }

}
