package org.activityinfo.shared.dto;

public class SchemaCsvWriter {

	private final StringBuilder csv = new StringBuilder();
	
	
	
	public void write(UserDatabaseDTO db) {
		writeHeaders();
		writeLine(db.getId(), null, "Database", null, db.getName(), db.getFullName(), null, null);
		
		for(ActivityDTO activity : db.getActivities()) {
			writeActivity(activity);
		}
		
	}
	
	private void writeActivity(ActivityDTO activity) {
		writeLine(activity.getId(), activity.getDatabase().getId(), "Activity", activity.getCategory(), activity.getName(), 
				null, null);
		
		for(IndicatorDTO indicator : activity.getIndicators()) {
			writeLine(indicator.getId(), activity.getId(), "Indicator", indicator.getCategory(), indicator.getName(),
					indicator.getDescription(), indicator.getUnits(), aggregationToString(indicator));
			
		}
		
		for(AttributeGroupDTO group : activity.getAttributeGroups()) {
			writeLine(group.getId(), activity.getId(), "AttributeGroup", group.getName(), 
					null, null, null);
			
			for(AttributeDTO attrib : group.getAttributes()) {
				writeLine(attrib.getId(), group.getId(), "Attribute", attrib.getName(),
						null, null, null);
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
		writeLine("Id", "ParentId", "Entity", "Category", "Name", "Description", "Units", "Aggregation");		
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
