package org.activityinfo.server.endpoint.refine;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.HttpHeaders;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

public class Utf8JacksonJsonProvider extends JacksonJsonProvider {

    public Utf8JacksonJsonProvider(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void writeTo(Object value, Class<?> type, Type genericType,
        Annotation[] annotations, MediaType mediaType,
        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
        throws IOException {

        httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, mediaType.toString() + ";charset=UTF-8");

        super.writeTo(value, type, genericType, annotations, mediaType, httpHeaders,
            entityStream);
        
    }
    
    

}
