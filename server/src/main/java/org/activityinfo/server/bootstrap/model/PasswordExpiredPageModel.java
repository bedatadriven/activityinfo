package org.activityinfo.server.bootstrap.model;

import org.activityinfo.server.database.hibernate.entity.User;

public class PasswordExpiredPageModel extends PageModel {

	private User user;

	public PasswordExpiredPageModel(User user) {
		super();
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
}
