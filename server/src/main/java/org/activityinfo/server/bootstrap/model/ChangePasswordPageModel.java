package org.activityinfo.server.bootstrap.model;

import org.activityinfo.server.database.hibernate.entity.User;

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
