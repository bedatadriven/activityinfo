package org.sigmah.server.bootstrap.model;

import org.sigmah.server.database.hibernate.entity.User;

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
