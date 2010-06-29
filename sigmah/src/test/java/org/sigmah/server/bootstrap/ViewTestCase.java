/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.BeforeClass;
import org.sigmah.server.bootstrap.model.PageModel;
import org.sigmah.server.util.TemplateModule;

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
