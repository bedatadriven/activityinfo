package org.activityinfo.shared.report.model;

import java.io.Serializable;

public class Parameter implements Serializable {

	public enum Type {
		DATE,
		DATABASE,
		ACTIVITY,
		INDICATOR
	}
	
	private String name;
	private Type type;
	private DateUnit dateUnit;

	public Parameter() {

	}

    /**
     *
     * @return The name of the parameter
     */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public DateUnit getDateUnit() {
		return dateUnit;
	}

	public void setDateUnit(DateUnit dateUnit) {
		this.dateUnit = dateUnit;
	}

}