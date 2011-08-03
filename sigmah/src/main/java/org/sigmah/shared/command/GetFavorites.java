package org.sigmah.shared.command;

import org.sigmah.shared.command.result.UserFavorites;

public class GetFavorites implements Command<UserFavorites> {
	private int userId;
	
	public GetFavorites() {
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}
}