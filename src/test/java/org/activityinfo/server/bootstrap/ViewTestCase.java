/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
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
    public static void setUpTemplateConfig() throws TemplateModelException  {
        TemplateModule module = new TemplateModule();
        templateCfg = module.provideConfiguration();
    }

    protected String process(PageModel model) throws IOException, TemplateException {
        FreemarkerViewProcessor processor = new FreemarkerViewProcessor(templateCfg, Providers.of(Locale.ENGLISH));
        Template template = processor.resolve(model.asViewable().getTemplateName());
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
