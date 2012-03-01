package org.sigmah.server.bootstrap.model;

import org.sigmah.server.database.hibernate.entity.User;

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
