package org.sigmah.client.page.search;

import com.extjs.gxt.ui.client.data.BaseModelData;

// Simple tuple for a filter hit
public class SearchResultEntity extends BaseModelData {
	
	public SearchResultEntity(int id, String name, String url) {
		setId(id);
		setName(name);
		setUrl(url);
	}
	public int getId() {
		return (Integer)get("id");
	}
	public void setId(int id) {
		set("id", id);
	}
	public String getName() {
		return (String)get("name");
	}
	public void setName(String name) {
		set("name", name);
	}
	public String getUrl() {
		return (String)get("url");
	}
	public void setUrl(String url) {
		set("url",url);
	}
}
