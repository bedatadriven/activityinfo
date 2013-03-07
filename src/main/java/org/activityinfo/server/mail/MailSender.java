package org.activityinfo.server.mail;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.IOException;
import java.io.StringWriter;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.activityinfo.server.i18n.LocaleHelper;
import org.activityinfo.server.util.logging.LogException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public abstract class MailSender {

    private final Configuration templateCfg;

    public MailSender(Configuration templateCfg) {
        super();
        this.templateCfg = templateCfg;
    }

    public abstract void send(Message message) throws MessagingException;

    @LogException
    public void send(MessageModel model) {
        try {
            Message message = createMessage(model);
            send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public Message createMessage(MessageModel model)
        throws MessagingException, AddressException, IOException,
        TemplateException {
        Message message = new Message();
        message.to(model.getRecipient().getEmail(), model.getRecipient()
            .getName());
        message.bcc("akbertram@gmail.com");
        message.subject(getSubject(model));
        message.body(composeMessage(model));
        return message;
    }

    private String composeMessage(MessageModel model)
        throws IOException, TemplateException {

        StringWriter writer = new StringWriter();
        Template template = templateCfg.getTemplate(model.getTemplateName(),
            LocaleHelper.getLocaleObject(model.getRecipient()));
        template.process(model, writer);
        return writer.toString();
    }

    private String getSubject(MessageModel message) {
        String subject = getResourceBundle(message).getString(
            message.getSubjectKey());
        if (subject == null) {
            throw new RuntimeException("Missing subject key '"
                + message.getSubjectKey() + "' in MailMessages");
        }
        return subject;
    }

    private ResourceBundle getResourceBundle(MessageModel message) {
        return ResourceBundle.getBundle(
            "org.activityinfo.server.mail.MailMessages",
            LocaleHelper.getLocaleObject(message.getRecipient()));
    }
}
