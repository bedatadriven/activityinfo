package org.activityinfo.server.bootstrap.jaxrs;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ResourceBundle;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.activityinfo.server.bootstrap.model.PageModel;
import org.activityinfo.server.bootstrap.model.TemplateDirective;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Provider
@Singleton
public class TemplateDirectiveMessageBodyWriter implements
		MessageBodyWriter<TemplateDirective> {
	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return TemplateDirective.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(TemplateDirective t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(TemplateDirective t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		Template template = t.getTemplate();
		PageModel model = t.getPageModel();

		try {
			template.getConfiguration().setSharedVariable("lang",
					template.getLocale().getLanguage());
			template.getConfiguration().setSharedVariable(
					"label",
					new freemarker.ext.beans.ResourceBundleModel(ResourceBundle
							.getBundle("template/page/Labels",
									template.getLocale()), new BeansWrapper()));

			template.process(model, new OutputStreamWriter(entityStream));
		} catch (TemplateException exc) {
			throw new WebApplicationException(exc, 500);
		}
	}
}
