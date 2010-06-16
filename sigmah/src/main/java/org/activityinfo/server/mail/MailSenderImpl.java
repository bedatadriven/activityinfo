package org.activityinfo.server.mail;

import org.activityinfo.server.util.logging.LogException;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

public class MailSenderImpl implements MailSender {

    @LogException
    public void send(Email email) throws EmailException {
        email.setHostName("localhost");
        email.setFrom("mailer@activityinfo.org", "ActivityInfo");
        email.send();
    }

}
