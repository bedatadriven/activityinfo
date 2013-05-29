package org.activityinfo.server.util.jaxrs;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.HttpHeaders;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.bedatadriven.geojson.GeoJsonModule;


/**
 * Wraps the JacksonJsonProvider to refine the ObjectMapper and 
 * to ensure that the Content-Type header always includes the 
 * charset=UTF-8 fragment
 */
public class Utf8JacksonJsonProvider extends JacksonJsonProvider {

    public Utf8JacksonJsonProvider() {
        super(createObjectMapper());
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
    
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new GeoJsonModule());
        return mapper;
    }

}
