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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.activityinfo.server.util.logging.LogException;
import org.apache.commons.codec.Charsets;

import com.google.inject.Inject;

import freemarker.template.Configuration;

/**
 * Sends mail messages by SMTP using the javax.mail API.
 *
 */
public class SmtpMailSender extends MailSender {
    private static final Logger LOGGER = Logger.getLogger(SmtpMailSender.class.getName());

    private final DeploymentConfiguration configuration;

    @Inject
    public SmtpMailSender(DeploymentConfiguration configuration,
        Configuration templateCfg) {
        super(templateCfg);
        this.configuration = configuration;
    }

    @Override
    @LogException
    public void send(Message message) {
        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setSubject(message.getSubject(), Charsets.UTF_8.name());
            mimeMessage.addRecipients(RecipientType.TO,
                toArray(message.getTo()));
            mimeMessage.addRecipients(RecipientType.BCC,
                toArray(message.getBcc()));
            mimeMessage.setFrom(new InternetAddress(
                configuration.getProperty("smtp.from"),
                configuration.getProperty("smtp.from.name")));

            if (message.getReplyTo() != null) {
                mimeMessage.setReplyTo(new Address[] { message.getReplyTo() });
            }

            String body;
            if (message.hasHtmlBody()) {
                body = message.getHtmlBody();
                mimeMessage.setDataHandler(new DataHandler(new HTMLDataSource(body)));
            } else {
                body = message.getTextBody();
                mimeMessage.setText(body, Charsets.UTF_8.name());
            }
            LOGGER.finest("message to " + message.getTo() + ":\n" + body);

            if (!message.getAttachments().isEmpty()) {
                Multipart multipart = new MimeMultipart();
                for (MessageAttachment attachment : message.getAttachments()) {
                    MimeBodyPart part = new MimeBodyPart();
                    part.setFileName(attachment.getFilename());

                    DataSource src = new ByteArrayDataSource(
                        attachment.getContent(), attachment.getContentType());
                    part.setDataHandler(new DataHandler(src));
                    multipart.addBodyPart(part);
                }
                mimeMessage.setContent(multipart);
            }
            mimeMessage.saveChanges();

            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private Address[] toArray(List<InternetAddress> to) {
        return to.toArray(new Address[to.size()]);
    }

    static class HTMLDataSource implements DataSource {
        private String html;

        public HTMLDataSource(String htmlString) {
            // transform non-ascii characters into entities to avoid
            // encoding issues

        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (html == null) {
                throw new IOException("Null HTML");
            }
            return new ByteArrayInputStream(html.getBytes());
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("This DataHandler cannot write HTML");
        }

        @Override
        public String getContentType() {
            return "text/html; charset=UTF-8";
        }

        @Override
        public String getName() {
            return "text/html dataSource";
        }
    }
}
