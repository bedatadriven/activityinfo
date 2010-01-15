/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.server.bootstrap;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.activityinfo.server.TemplateModule;
import org.activityinfo.server.bootstrap.model.PageModel;
import org.junit.BeforeClass;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Alex Bertram
 */
public abstract class ViewTestCase {

    protected static Configuration templateCfg;

    @BeforeClass
    public static void setUpTemplateConfig() {
        TemplateModule module = new TemplateModule();
        templateCfg = module.provideConfiguration();
    }

    protected String process(PageModel model) throws IOException, TemplateException {
        Template template = templateCfg.getTemplate(model.getTemplateName());
        StringWriter writer = new StringWriter();
        template.process(model, writer);

        return writer.toString();
    }

    protected void assertProcessable(PageModel model) {
        try {
            process(model);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
