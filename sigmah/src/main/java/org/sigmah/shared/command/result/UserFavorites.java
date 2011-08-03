package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.PageDTO;

public class UserFavorites implements CommandResult {
	private List<PageDTO> favorites;

	public UserFavorites() {
	}

	public UserFavorites(List<PageDTO> favorites) {
		this.favorites = favorites;
	}

	public void setFavorites(List<PageDTO> favorites) {
		this.favorites = favorites;
	}

	public List<PageDTO> getFavorites() {
		return favorites;
	}
	
}
