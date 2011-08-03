package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class PageDTO extends BaseModelData implements DTO{
	
	public PageDTO() {
		super();
	}
	
	public String getPageId() {
		return (String)get("pageId");
	}
	public void setPageId(String pageId) {
		set("pageId", pageId);
	}
	public String getName() {
		return (String)get("name");
	}
	public void setName(String name) {
		set("name", name);
	}
}
