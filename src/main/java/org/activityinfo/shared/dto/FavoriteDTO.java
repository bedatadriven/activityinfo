package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class FavoriteDTO extends BaseModelData {
	@SuppressWarnings("unused")
	private FavoriteType favoritetypedummyfieldforgwtserialization;
	
	public FavoriteDTO() {
		super();
	}
	public FavoriteDTO setName(String name) {
		set("name", name);
		return this;
	}
	public String getName() {
		return (String)get("name");
	}
	
	public FavoriteDTO setId(int id) {
		set("id", id);
		return this;
	}
	public int getId() {
		return (Integer) get("id");
	}
	
	public FavoriteDTO setFavoriteType(FavoriteType favoriteType) {
		set("favoriteType", favoriteType);
		return this;
	}
	public FavoriteType getFavoriteType() {
		return (FavoriteType) get("favoriteType");
	}
}
