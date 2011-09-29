package org.sigmah.client.page.entry;

import org.sigmah.shared.dto.SiteDTO;

public class YearViewModel extends SiteDTO {
	
	public YearViewModel() {
		super();
	}
	public YearViewModel(SiteDTO site) {
		super(site);
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
		setId(year);
		return this;
	}
	public int getYear() {
		return (Integer)get("year");
	}
}
