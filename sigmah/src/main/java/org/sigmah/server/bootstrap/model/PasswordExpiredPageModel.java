package org.sigmah.server.bootstrap.model;

import org.sigmah.shared.domain.User;

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
