package org.activityinfo.server.endpoint.rest;

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

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;

/**
 * Workaround for Swagger, which requires ServletConfig to be injected.
 * Some how this doesn't work with Guice Container so we'll just inject 
 * what we need.
 *
 */
@Provider
@Singleton
public class SwaggerConfigProvider extends
    SingletonTypeInjectableProvider<Context, ServletConfig> {

    @Inject
    public SwaggerConfigProvider(ServletContext servletContext) {
        super(ServletConfig.class, new Config(servletContext));
    }

    private static class Config implements ServletConfig {

        private Map<String, String> initParams = Maps.newHashMap();
        private ServletContext servletContext;

        public Config(ServletContext servletContext) {
            this.servletContext = servletContext;
        }

        @Override
        public String getInitParameter(String key) {
            return initParams.get(key);
        }

        @Override
        public Enumeration getInitParameterNames() {
            return Collections.enumeration(initParams.keySet());
        }

        @Override
        public ServletContext getServletContext() {
            return servletContext;
        }

        @Override
        public String getServletName() {
            return "ActivityInfo";
        }

    }

}
