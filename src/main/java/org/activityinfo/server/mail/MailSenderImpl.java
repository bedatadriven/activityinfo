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

import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.activityinfo.server.util.logging.LogException;

import com.google.inject.Inject;

import freemarker.template.Configuration;

public class MailSenderImpl extends MailSender {

    private final DeploymentConfiguration configuration;

    @Inject
    public MailSenderImpl(DeploymentConfiguration configuration,
        Configuration templateCfg) {
        super(templateCfg);
        this.configuration = configuration;
    }

    @Override
    @LogException
    public void send(Message message) {
        try {
            message.setFrom(new InternetAddress(
                configuration.getProperty("smtp.from"),
                configuration.getProperty("smtp.from.name")));

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
