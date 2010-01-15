package org.activityinfo.server.mail;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

public interface MailSender {

    public void send(Email message) throws EmailException;
}
