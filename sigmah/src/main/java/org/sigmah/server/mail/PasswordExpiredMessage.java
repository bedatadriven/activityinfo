package org.sigmah.server.mail;

import org.sigmah.server.database.hibernate.entity.User;

public class PasswordExpiredMessage extends MailMessage {

	private User user;
	
	
	public PasswordExpiredMessage(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public User getRecipient() {
		return user;
	}


}
