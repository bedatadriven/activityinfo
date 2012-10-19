package org.activityinfo.server.endpoint.jsonrpc.serde;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map.Entry;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ModelDataSerializer implements JsonSerializer<ModelData> {

	@Override
	public JsonElement serialize(ModelData src, Type typeOfSrc,
			JsonSerializationContext context) {
		
		JsonObject object = new JsonObject();
		for(Entry<String,Object> property : src.getProperties().entrySet()) {
			object.add(property.getKey(), context.serialize(property.getValue()));
		}
		
		for(Field field : src.getClass().getFields()) {
			field.setAccessible(true);
			if(!Modifier.isStatic(field.getModifiers()) &&
			   !Modifier.isTransient(field.getModifiers()) &&
					field.getDeclaringClass().getPackage().getName().startsWith("org.activityinfo")) {
				
				try {
					object.add(field.getName(), context.serialize(field.get(src)));
				} catch (Exception e) {
				}
			}
		}
		return object;
		
		
	}

	
}
