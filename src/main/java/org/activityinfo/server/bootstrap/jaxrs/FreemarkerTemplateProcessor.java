package org.activityinfo.server.bootstrap.jaxrs;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Logger;

import javax.ws.rs.ext.Provider;

import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.spi.template.ViewProcessor;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Match a Viewable-named view with a Freemarker template.
 *
 */
@Provider
@Singleton
public class FreemarkerTemplateProcessor implements ViewProcessor<Template> {

    private static final Logger LOGGER = Logger.getLogger( FreemarkerTemplateProcessor.class.getName() );

    private final Configuration templateConfig;
    
    @Inject
	public FreemarkerTemplateProcessor(Configuration templateConfig) {
		super();
		this.templateConfig = templateConfig;
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
			t.process(viewable.getModel(), writer);
			writer.flush();
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}
}