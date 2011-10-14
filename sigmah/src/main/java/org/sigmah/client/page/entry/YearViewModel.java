package org.sigmah.client.page.entry;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class YearViewModel extends BaseModelData {
	public YearViewModel() {
		super();
	}
	public String getName() {
		return (String)get("name");
	}
	public YearViewModel setName(String name) {
		set("name", name);
		return this;
	}
	public YearViewModel setYear(int year) {
		set("year", year);
		return this;
	}
	public int getYear() {
		return (Integer)get("year");
	}
}
