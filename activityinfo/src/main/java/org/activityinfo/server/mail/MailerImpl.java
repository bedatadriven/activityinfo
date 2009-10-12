package org.activityinfo.server.mail;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

public class MailerImpl implements Mailer {

	public void send(Email email) throws EmailException {
	    email.setHostName("localhost");
        email.setFrom("mailer@activityinfo.org", "ActivityInfo");
        email.send();
	}

}
