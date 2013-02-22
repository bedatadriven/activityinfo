package org.activityinfo.server.bootstrap;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import org.activityinfo.server.bootstrap.jaxrs.FreemarkerViewProcessor;
import org.activityinfo.server.bootstrap.model.PageModel;
import org.activityinfo.server.util.TemplateModule;
import org.bouncycastle.util.Strings;
import org.junit.BeforeClass;

import com.google.inject.util.Providers;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * @author Alex Bertram
 */
public abstract class ViewTestCase {

    protected static Configuration templateCfg;

    @BeforeClass
    public static void setUpTemplateConfig() throws TemplateModelException {
        TemplateModule module = new TemplateModule();
        templateCfg = module.provideConfiguration();
    }

    protected String process(PageModel model) throws IOException,
        TemplateException {
        FreemarkerViewProcessor processor = new FreemarkerViewProcessor(
            templateCfg, Providers.of(Locale.ENGLISH));
        Template template = processor.resolve(model.asViewable()
            .getTemplateName());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        processor.writeTo(template, model.asViewable(), baos);

        return Strings.fromUTF8ByteArray(baos.toByteArray());
    }

    protected void assertProcessable(PageModel model) {
        try {
            process(model);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
