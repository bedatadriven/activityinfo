package org.activityinfo.server.branding;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.activityinfo.server.database.hibernate.entity.Domain;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ScaffoldingDirective implements TemplateDirectiveModel {

	private Provider<Domain> domainProvider;
	private Configuration templateConfiguration;
	private Template defaultScaffoldingTemplate;
	
	@Inject
	public ScaffoldingDirective(Provider<Domain> domainProvider,
			Configuration templateConfiguration) throws IOException {
		super();
		this.domainProvider = domainProvider;
		this.templateConfiguration = templateConfiguration;
		this.defaultScaffoldingTemplate = templateConfiguration.getTemplate("/page/DefaultScaffolding.ftl");
	}
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		
		// write out the inner body to a string
		StringWriter bodyWriter = new StringWriter();
		body.render(bodyWriter);
		
		env.setVariable("body", new SimpleScalar(bodyWriter.toString()));
		env.setVariable("title", (TemplateModel)params.get("title"));
		env.include(getTemplate());
				
	}

	private Template getTemplate() {
		String scaffoldingTemplateSource = domainProvider.get().getScaffolding();
		if(Strings.isNullOrEmpty(scaffoldingTemplateSource)) {
			return defaultScaffoldingTemplate;
		} else {
			try {
				return new Template(domainProvider.get().getHost(), scaffoldingTemplateSource, templateConfiguration);
			} catch (IOException e) {
				throw new RuntimeException("Exception creating custom scaffolding template", e);
			}
		}
	}

}
