package org.sigmah.client.page.report.json;

import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportFrequency;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;


public class ReportJsonFactory implements ReportSerializer {

	private final JsonParser parser;

	public ReportJsonFactory(){
		 parser = new JsonParser();
	}

	@Override
	public String serialize(Report element) {
		JsonObject object = new JsonObject();
    
		// write custom maker
		object.add("id", new JsonPrimitive(element.getId()));
        object.add("description", new JsonPrimitive(element.getDescription()));
        object.add("fileName", new JsonPrimitive(element.getFileName()));
        
        if(element.getDay()!=null){
        	object.add("day", new JsonPrimitive(element.getDay()));	
        }
        // TODO umad, complete the serialization on all elements of Report Object
        // get elements one by one and add them to json OBJECT.
		return object.toString();
	}

	@Override
	public Report deserialize(String json) {

		JsonElement jsonElement = parser.parse(json);
		
JsonObject object = jsonElement.getAsJsonObject();
		
		int id = object.get("id").getAsInt();
		object.get("filters").getAsJsonArray();
		
	//	JsonArray array = jsonElement.getAsJsonArray();
		// loop through all elements
		
		
		
		Report report = new Report();
		
		return report;
	}


}
