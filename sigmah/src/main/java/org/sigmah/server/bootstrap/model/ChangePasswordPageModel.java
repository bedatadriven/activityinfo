package org.sigmah.server.bootstrap.model;

import org.sigmah.shared.domain.User;

public class ChangePasswordPageModel extends PageModel {
	private User user;

	public ChangePasswordPageModel(User user) {
		super();
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
