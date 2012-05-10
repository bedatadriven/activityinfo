package org.activityinfo.server.mail;

import org.activityinfo.server.database.hibernate.entity.User;

public class ResetPasswordMessage extends MailMessage {
    private User user;

	public ResetPasswordMessage(User user) {
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
