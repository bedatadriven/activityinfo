package org.activityinfo.server.endpoint.jsonrpc.serde;

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
