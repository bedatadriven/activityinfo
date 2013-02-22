package org.activityinfo.server.endpoint.refine;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
