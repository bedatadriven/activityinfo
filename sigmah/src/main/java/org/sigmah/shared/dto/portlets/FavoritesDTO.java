package org.sigmah.shared.dto.portlets;

import java.util.List;

import org.sigmah.shared.dto.PageDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class FavoritesDTO extends BaseModelData implements PortletDTO {
	public FavoritesDTO() {
		super();
	}

	@Override
	public String getName() {
		return "Saved pages";
	}

	@Override
	public String getDescription() {
		return "Displays a list of \"favorites\": pages the user has saved for quick access";
	}

	public void setFavorites(List<PageDTO> favorites) {
		set("favorites", favorites);
	}

	@SuppressWarnings("unchecked")
	public List<PageDTO> getFavorites() {
		return (List<PageDTO>)get("favorites");
	}
}