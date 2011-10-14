package org.sigmah.client.page.entry;

import com.extjs.gxt.ui.client.data.BaseModelData;


public class MonthViewModel extends BaseModelData {
	public MonthViewModel() {
		super();
	}
	public String getName() {
		return (String)get("name");
	}
	public MonthViewModel setName(String name) {
		set("name", name);
		return this;
	}
	public MonthViewModel setYear(int year) {
		set("year", year);
		return this;
	}
	public int getYear() {
		return (Integer)get("year");
	}
	public MonthViewModel setMonth(int month) {
		set("month", month);
		return this;
	}
	public int getMonth() {
		return (Integer) get("month");
	}
}
