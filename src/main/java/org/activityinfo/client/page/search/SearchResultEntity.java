package org.activityinfo.client.page.search;

import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 *  Simple tuple for a filter hit
 */
public final class SearchResultEntity extends BaseModelData {
	
	public SearchResultEntity(int id, String name, String url, DimensionType dimensionType) {
		setId(id);
		setName(name);
		setUrl(url);
		setDimensionType(dimensionType);
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
	public DimensionType getDimension() {
		return (DimensionType) get("dimensionType");
	}
	public void setDimensionType(DimensionType dimensionType) {
		set("dimensionType", dimensionType);
	}
}
