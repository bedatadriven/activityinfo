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
