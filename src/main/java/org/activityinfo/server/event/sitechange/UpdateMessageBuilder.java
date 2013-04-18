package org.activityinfo.server.event.sitechange;

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

import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.entry.form.SiteRenderer;
import org.activityinfo.client.page.entry.form.SiteRenderer.IndicatorValueFormatter;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.i18n.LocaleHelper;
import org.activityinfo.server.mail.Message;
import org.activityinfo.server.util.html.HtmlWriter;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.teklabs.gwt.i18n.server.LocaleProxy;

public class UpdateMessageBuilder {

    private static final Logger LOGGER = Logger.getLogger(UpdateMessageBuilder.class
        .getName());

    private ChangeType type;
    private User recipient;
    private User editor;
    private Date date;
    private SiteDTO siteDTO;
    private ActivityDTO activityDTO;
    private UserDatabaseDTO userDatabaseDTO;

    public void setChangeType(ChangeType type) {
        this.type = type;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public void setEditor(User editor) {
        this.editor = editor;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSiteDTO(SiteDTO siteDTO) {
        this.siteDTO = siteDTO;
    }

    public void setActivityDTO(ActivityDTO activityDTO) {
        this.activityDTO = activityDTO;
    }

    public void setUserDatabaseDTO(UserDatabaseDTO userDatabaseDTO) {
        this.userDatabaseDTO = userDatabaseDTO;
    }

    public Message build() throws MessagingException  {
        // set the locale of the messages
        LocaleProxy.setLocale(LocaleHelper.getLocaleObject(recipient));

        // create message, set recipient & bcc
        Message message = new Message();
        message.to(recipient.getEmail(), recipient.getName());

        // set the subject
        String subject;
        switch (type) {
        case CREATE:
            subject = I18N.MESSAGES.newSiteSubject(userDatabaseDTO.getName(),
                activityDTO.getName(), siteDTO.getLocationName(),
                siteDTO.getPartnerName());
            break;
        case DELETE:
            subject = I18N.MESSAGES.deletedSiteSubject(
                userDatabaseDTO.getName(), activityDTO.getName(),
                siteDTO.getLocationName());
            break;
        default: // UPDATE
            subject = I18N.MESSAGES.updatedSiteSubject(
                userDatabaseDTO.getName(), activityDTO.getName(),
                siteDTO.getLocationName());
            break;
        }
        message.subject(subject);

        // create the html body
        HtmlWriter htmlWriter = new HtmlWriter();

        htmlWriter.startDocument();

        htmlWriter.startDocumentHeader();
        htmlWriter.documentTitle(subject);
        htmlWriter.endDocumentHeader();

        htmlWriter.startDocumentBody();

        String greeting = I18N.MESSAGES.sitechangeGreeting(recipient.getName());
        htmlWriter.paragraph(greeting);

        String intro;
        switch (type) {
        case CREATE:
            intro = I18N.MESSAGES.siteCreateIntro(editor.getName(),
                editor.getEmail(), activityDTO.getName(),
                siteDTO.getLocationName(),
                userDatabaseDTO.getName(), date);
            break;
        case DELETE:
            intro = I18N.MESSAGES.siteDeleteIntro(editor.getName(),
                editor.getEmail(), activityDTO.getName(),
                siteDTO.getLocationName(),
                userDatabaseDTO.getName(), date);
            break;
        default: // UPDATE
            intro = I18N.MESSAGES.siteChangeIntro(editor.getName(),
                editor.getEmail(), activityDTO.getName(),
                siteDTO.getLocationName(),
                userDatabaseDTO.getName(), date);
        }
        htmlWriter.paragraph(intro);

        if (type.isNewOrUpdate()) {
            SiteRenderer siteRenderer = new SiteRenderer();
            siteRenderer.setIndicatorValueFormatter(new IndicatorValueFormatter() {
                @Override
                public String format(Double value) {
                    return new DecimalFormat("#,##0.####").format(value);
                }
            });

            htmlWriter.paragraph(siteRenderer.renderLocation(siteDTO, activityDTO));
            htmlWriter.paragraph(siteRenderer.renderSite(siteDTO, activityDTO, false, true));
        }

        String url = "http://www.activityinfo.org/#data-entry/Activity+"
            + activityDTO.getId();

        htmlWriter
            .paragraph("<a href=\"" + url + "\">Open in ActivityInfo</a>");

        String signature = I18N.MESSAGES.sitechangeSignature();
        htmlWriter.paragraph(signature);

        htmlWriter.endDocumentBody();
        htmlWriter.endDocument();

        LOGGER.fine("Message:\n" + htmlWriter.toString());

        message.htmlBody(htmlWriter.toString());

        return message;
    }
}
