package org.activityinfo.server.endpoint.refine;

//example from freebase:
//	{
//	      "expected_type" : {
//	        "/freebase/type_hints/mediator" : true,
//	        "id" : "/measurement_unit/dated_money_value",
//	        "name" : "Dated Money Value"
//	      },
//	      "id" : "/location/statistical_region/rent50_0",
//	      "name" : "50th percentile rent - 0 br",
//	      "schema" : [
//	        {
//	          "id" : "/location/statistical_region",
//	          "name" : "Statistical region"
//	        }
//	      ],
//	      "type" : "/type/property"
//	}
	

public class PropertyDescription {
	private String id;
	private String name;
	
	public PropertyDescription(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
