package org.activityinfo.server.service.impl;

import org.activityinfo.server.mail.Mailer;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

public class NullMailer implements Mailer {

    public void send(Email message) throws EmailException {
        // NOOP
    }
}
