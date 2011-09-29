package org.sigmah.client.page.entry;

import org.sigmah.shared.dto.SiteDTO;

public class MonthViewModel extends SiteDTO {
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
		setId(Integer.parseInt(Integer.toString(getYear()) + month));
		return this;
	}
	public int getMonth() {
		return (Integer) get("month");
	}
}
