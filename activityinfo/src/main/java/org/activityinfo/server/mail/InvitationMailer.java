/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.activityinfo.server.domain.User;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.ResourceBundle;

public class InvitationMailer {

    private final Configuration templateCfg;
    private final MailSender sender;
    static final String TEXT_TEMPLATE = "mail/invite.ftl";

    public InvitationMailer(Configuration templateCfg, MailSender sender) {
        this.templateCfg = templateCfg;
        this.sender = sender;
    }

    public void sendInvitation(User newUser, User invitingUser)
            throws EmailException, TemplateException, IOException {

        Locale locale = newUser.getLocaleObject();
        ResourceBundle mailMessages = getResourceBundle(locale);

        SimpleEmail mail = new SimpleEmail();
        mail.addTo(newUser.getEmail(), newUser.getName());
        mail.addBcc("akbertram@gmail.com"); // for testing purposes
        mail.setSubject(mailMessages.getString("newUserSubject"));

        Invitation model = new Invitation(newUser, invitingUser);
        mail.setMsg(composeMessage(model, locale));

        sender.send(mail);
    }

    private ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle("org.activityinfo.server.mail.MailMessages", locale);
    }

    private String composeMessage(Invitation model, Locale locale)
            throws IOException, TemplateException {

        StringWriter writer = new StringWriter();
        Template template = templateCfg.getTemplate(TEXT_TEMPLATE, locale);
        template.process(model, writer);
        return writer.toString();
    }
}
