package org.activityinfo.server.auth;

import org.activityinfo.server.mail.MailSender;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

public class NullMailer implements MailSender {

    public void send(Email message) throws EmailException {
        // NOOP
    }
}
