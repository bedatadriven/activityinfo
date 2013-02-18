package org.activityinfo.server.bootstrap.jaxrs;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.activityinfo.server.bootstrap.model.Redirect;

@Provider
@Singleton
public class RedirectMessageBodyWriter implements MessageBodyWriter<Redirect> {
	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return Redirect.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(Redirect t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Redirect t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		httpHeaders.remove(javax.ws.rs.core.HttpHeaders.LOCATION);
		httpHeaders.add(javax.ws.rs.core.HttpHeaders.LOCATION, t.getLocation());
	}
}
