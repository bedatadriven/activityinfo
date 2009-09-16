package org.activityinfo.shared.dto;

import java.io.Serializable;

public class ReportParameterDTO implements DTO {



	private String name;
	private String label;
	private int type;
	private int dateUnit;
	
	public static final int TYPE_DATE = 1;
	public static final int TYPE_DATABASE = 2;
	public static final int TYPE_ACTIVITY = 3;
	public static final int TYPE_INDICATOR = 4;
	public static final int TYPE_ATTRIBUTE = 5;
	

	public static final int UNIT_DAY = 0;
	public static final int UNIT_MONTH = 1;
	public static final int UNIT_QUARTER = 2;
	public static final int UNIT_YEAR = 3;
	public static final int UNIT_WEEK = 4;
		
	public ReportParameterDTO() {
		
	}
	
	public ReportParameterDTO(String name, String label, int type) {
		this.name = name;
		this.label = label;
		this.type = type;
	}

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public void setDateUnit(int unit) {
		this.dateUnit = unit;
	}
	
	public int getDateUnit(){ 
		return this.dateUnit;
	}
}
