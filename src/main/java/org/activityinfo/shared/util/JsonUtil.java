package org.activityinfo.shared.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.activityinfo.client.Log;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtil {
	
	private JsonUtil() {}
	
	public static JsonObject encodeMap(Map<String, Object> map) {
		JsonObject root = new JsonObject();
		for(Entry<String,Object> property : map.entrySet()) {
			if(property.getValue() != null) {
				JsonObject value = new JsonObject();
				
				if(property.getValue() instanceof String) {
					value.addProperty("type", "String");
					value.addProperty("value", (String)property.getValue());
				} else if(property.getValue() instanceof Integer ) {
					value.addProperty("type", "Integer");
					value.addProperty("value", (Integer)property.getValue());
				} else if(property.getValue() instanceof Double) {
					value.addProperty("type", "Double");
					value.addProperty("value", (Double)property.getValue());
				} else if(property.getValue() instanceof Date) {
					Date date = (Date)property.getValue();
	
					value.addProperty("type", "Date");
					value.addProperty("time", date.getTime());
				} else if(property.getValue() instanceof Boolean) {
					value.addProperty("type", "Boolean");
					value.addProperty("value", (Boolean)property.getValue());
				} else if(property.getValue() instanceof LocalDate) {
					value.addProperty("type", "LocalDate");
					value.addProperty("value", property.getValue().toString());
					
				} else {
          Log.error("Cannot convert handle map value '" + property.getKey() + ", type " + property.getKey() +
							": " + property.getValue().getClass().getName());
          value = null;
				}

        if(value != null ) {
				  root.add(property.getKey(), value);
        }
			}
		}
		return root;
	}
	
	public static Map<String,Object> decodeMap(JsonObject root) {
		Map<String,Object> map = new HashMap<String, Object>();
		for(Entry<String,JsonElement> property : root.entrySet()) {
			JsonObject value = (JsonObject)property.getValue();
			String type = value.get("type").getAsString();
			
			if("String".equals(type)) {
				map.put(property.getKey(), value.get("value").getAsString());
			} else if("Integer".equals(type)) {
				map.put(property.getKey(), value.get("value").getAsInt());
			} else if("Double".equals(type)) {
				map.put(property.getKey(), value.get("value").getAsDouble());
			} else if("Date".equals(type)) {
				map.put(property.getKey(), new Date(value.get("time").getAsLong()));
			} else if("Boolean".equals(type)) {
				map.put(property.getKey(), value.get("value").getAsBoolean());
			} else if("LocalDate".equals(type)) {
				map.put(property.getKey(), LocalDate.parse( value.get("value").getAsString()));
			} else {
				throw new IllegalArgumentException("map contains key with unsupported value type -- " + 
					property.getKey() + ": " + type);
			}
			
		}
		return map;
	}
}
