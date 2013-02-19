package org.activityinfo.server.bootstrap.jaxrs;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.ws.rs.ext.Provider;

import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
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

    private static final Logger LOGGER = Logger.getLogger( FreemarkerViewProcessor.class.getName() );

    private final Configuration templateConfig;
    private final javax.inject.Provider<Locale> localeProvider;
    
    @Inject
	public FreemarkerViewProcessor(Configuration templateConfig, javax.inject.Provider<Locale> localeProvider) {
		super();
		this.templateConfig = templateConfig;
		this.localeProvider = localeProvider;

	}

	@Override
	public Template resolve(String name) {
		try {
			if(name.startsWith("/")) {
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
		
		Writer writer = new OutputStreamWriter(out, Charsets.UTF_8);
		try {
		    Environment env = t.createProcessingEnvironment(viewable.getModel(), writer);
		    env.setLocale(localeProvider.get());
	        env.setVariable("label", getResourceBundle(localeProvider.get()));		    
	        env.process();
			writer.flush();
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}

    private ResourceBundleModel getResourceBundle(Locale locale) {
        return new freemarker.ext.beans.ResourceBundleModel(ResourceBundle.getBundle("template/page/Labels", locale), 
                new BeansWrapper());
    }
}