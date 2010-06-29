/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.mock;

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
