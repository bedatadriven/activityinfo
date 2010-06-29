package org.sigmah.server.mail;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.sigmah.server.util.logging.LogException;

public class MailSenderImpl implements MailSender {

    @LogException
    public void send(Email email) throws EmailException {
        email.setHostName("localhost");
        email.setFrom("mailer@activityinfo.org", "ActivityInfo");
        email.send();
    }

}
