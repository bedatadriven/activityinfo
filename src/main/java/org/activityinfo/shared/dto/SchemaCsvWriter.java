package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.ModelData;

public class SchemaCsvWriter {

	private final StringBuilder csv = new StringBuilder();
	
	public void write(UserDatabaseDTO db) {
		writeHeaders();
		
		for(ActivityDTO activity : db.getActivities()) {
			writeActivity(activity);
		}
		
	}
	
	private void writeActivity(ActivityDTO activity) {

		
		for(IndicatorDTO indicator : activity.getIndicators()) {
			writeElementLine(activity, indicator);
			
		}
		
		for(AttributeGroupDTO group : activity.getAttributeGroups()) {
			for(AttributeDTO attrib : group.getAttributes()) {
				writeElementLine(activity, group, attrib);
			}
		}
	}

	private String aggregationToString(IndicatorDTO indicator) {
		switch(indicator.getAggregation()) {
		case IndicatorDTO.AGGREGATE_SITE_COUNT:
			return "Count of Sites";
		case IndicatorDTO.AGGREGATE_AVG:
			return "Average";
		case IndicatorDTO.AGGREGATE_SUM:
			return "Sum";
		}
		return "-";
	}

	private void writeHeaders() {
		writeLine("DatabaseId", "DatabaseName", 
				"ActivityId", "ActivityCategory", "ActivityName", 
				"FormFieldType",
				"AttributeGroup/IndicatorId", 
				"Category",
				"Name",
				"Description",
				"Units", 
				"AttributeId",
				"AttributeValue");		
	}
	
	private void writeElementLine(ActivityDTO activity, IndicatorDTO indicator) {
		writeLine(
				activity.getDatabase().getId(), activity.getDatabase().getName(), 
				activity.getId(), activity.getCategory(), activity.getName(),
				"Indicator",
				indicator.getId(), indicator.getCategory(), indicator.getName(), indicator.getDescription(), indicator.getUnits(), 
				null, null);
	}
	
	private void writeElementLine(ActivityDTO activity, AttributeGroupDTO attribGroup, AttributeDTO attrib) {
		writeLine(
				activity.getDatabase().getId(), activity.getDatabase().getName(), 
				activity.getId(), activity.getCategory(), activity.getName(),
				"AttributeGroup",
				attribGroup.getId(), null, attribGroup.getName(), null, null, 
				attrib.getId(), attrib.getName());
	}
	
	private void writeLine(Object... columns) {
		
		for(int i=0;i!=columns.length;++i) {
			if(i > 0) {
				csv.append(",");
			}
			Object val = columns[i];
			if(val!=null) {
				if(val instanceof String) {
					String escaped = ((String)val).replace("\"", "\"\"");
					csv.append("\"").append(escaped).append("\"");
				} else {
					csv.append(val.toString());
				}
			}
		}
		csv.append("\n");
	}
	
	public String toString() {
		return csv.toString();
	}
	
}
