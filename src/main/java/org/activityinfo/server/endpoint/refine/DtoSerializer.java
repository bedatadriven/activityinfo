package org.activityinfo.server.endpoint.refine;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.Model;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Observable;

/**
 * Serializes GXT-based ModelData classes where properties
 * are stored in both fields and the RpcMap.
 */
public class DtoSerializer extends JsonSerializer<ModelData> {

    @Override
    public Class<ModelData> handledType() {
        return ModelData.class;
    }

    @Override
    public void serialize(ModelData value, JsonGenerator jgen,
        SerializerProvider provider) throws IOException,
        JsonProcessingException {
       
        jgen.writeStartObject();
        try {
            for(PropertyDescriptor propertyDescriptor : 
                Introspector.getBeanInfo(value.getClass()).getPropertyDescriptors()) {

                Method getter = propertyDescriptor.getReadMethod();
                if(isSerializable(propertyDescriptor)) {
                  
                    Object fieldValue = getter.invoke(value);
                    if(!isEmptyish(fieldValue) && (isPropertyBacked(propertyDescriptor,value) ||
                        isFieldBacked(propertyDescriptor, value))) {
                        
                        jgen.writeFieldName(propertyDescriptor.getName());
                        jgen.writeObject(fieldValue);
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
        jgen.writeEndObject();
 
    }

    private boolean isFieldBacked(PropertyDescriptor propertyDescriptor, ModelData value) {
        try {
            return value.getClass().getField(propertyDescriptor.getName()) != null;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isPropertyBacked(PropertyDescriptor propertyDescriptor,
        ModelData value) {
       
        return value.getPropertyNames().contains(propertyDescriptor.getName());
    }

    private boolean isEmptyish(Object fieldValue) {
        if(fieldValue == null) {
            return true;
        }
        if(fieldValue instanceof Collection && ((Collection) fieldValue).isEmpty()) {
            return true;
        }
        if(fieldValue instanceof Map && ((Map) fieldValue).isEmpty()) {
            return true;
        }
        if(fieldValue instanceof CharSequence && ((CharSequence) fieldValue).length() == 0) {
            return true;
        }
        return false;
    }

    private boolean isSerializable(PropertyDescriptor propertyDescriptor) {
        Method getter = propertyDescriptor.getReadMethod();
        if(getter == null) {
            return false;
        }
        Class declaringClass = getter.getDeclaringClass();
        if(declaringClass.equals(ModelData.class) ||
            declaringClass.equals(BaseModelData.class) ||
            declaringClass.equals(Model.class) ||
            declaringClass.equals(Observable.class) ||
            declaringClass.equals(ModelData.class) ||
            declaringClass.equals(Object.class)) {
            return false;
        }
        
        return true;
    }
}
