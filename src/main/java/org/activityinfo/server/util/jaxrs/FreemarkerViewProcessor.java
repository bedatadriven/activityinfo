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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.spi.template.ViewProcessor;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.ResourceBundleModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Match a Viewable-named view with a Freemarker template.
 * 
 */
@Provider
@Singleton
public class FreemarkerViewProcessor implements ViewProcessor<Template> {

    private static final Logger LOGGER = Logger
        .getLogger(FreemarkerViewProcessor.class.getName());

    private final Configuration templateConfig;
    private final javax.inject.Provider<Locale> localeProvider;
   
    private @Context HttpContext httpContext;

    @Inject
    public FreemarkerViewProcessor(Configuration templateConfig,
        javax.inject.Provider<Locale> localeProvider) {
        super();
        this.templateConfig = templateConfig;
        this.localeProvider = localeProvider;

    }

    @Override
    public Template resolve(String name) {
        try {
            if (name.startsWith("/")) {
                name = name.substring(1);
            }
            return templateConfig.getTemplate(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeTo(Template t, Viewable viewable, OutputStream out)
        throws IOException {

        // ensure that we set an content type and charset
        httpContext.getResponse().getHttpHeaders()
        .putSingle(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8");
        
        Writer writer = new OutputStreamWriter(out, Charsets.UTF_8);
        try {
            Environment env = t.createProcessingEnvironment(
                viewable.getModel(), writer);
            env.setLocale(localeProvider.get());
            env.setVariable("label", getResourceBundle(localeProvider.get()));
            env.process();
            writer.flush();
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    private ResourceBundleModel getResourceBundle(Locale locale) {
        return new freemarker.ext.beans.ResourceBundleModel(
            ResourceBundle.getBundle("template/page/Labels", locale),
            new BeansWrapper());
    }
}