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

package org.activityinfo.server.mock;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;

/**
 * @author Alex Bertram
 */
public class MockTemplateConfiguration extends Configuration {

    public String lastTemplateName;
    public Object lastModel;

    @Override
    public Template getTemplate(String name) throws IOException {

        this.lastTemplateName = name;

        Template template = new Template(name, new StringReader(""), this) {

            @Override
            public void process(Object rootMap, Writer out) throws TemplateException, IOException {
                lastModel = rootMap;
            }
        };

        return template;
    }
}
