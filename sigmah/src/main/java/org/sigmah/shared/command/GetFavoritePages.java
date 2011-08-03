package org.sigmah.shared.command;

import org.sigmah.shared.command.result.UserFavorites;


public class GetFavoritePages implements Command<UserFavorites> {
	private int userId;
	
	public GetFavoritePages() {
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}
}