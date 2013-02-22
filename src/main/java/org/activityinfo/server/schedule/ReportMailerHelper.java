package org.activityinfo.server.schedule;

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

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.activityinfo.server.database.hibernate.entity.ReportSubscription;
import org.activityinfo.server.i18n.LocaleHelper;
import org.activityinfo.server.util.html.HtmlWriter;
import org.activityinfo.shared.report.model.EmailDelivery;
import org.activityinfo.shared.report.model.Report;

public class ReportMailerHelper {

    /**
     * Checks if the given ReportSubscription should be mailed today.
     * 
     * @param dateToday
     *            Today's date
     * @param report
     *            The report for which to check
     * @return True if the report should be mailed.
     */
    public static boolean mailToday(Date dateToday, ReportSubscription report) {

        Calendar today = Calendar.getInstance();
        today.setTime(dateToday);

        if (report.getEmailDelivery() == EmailDelivery.WEEKLY) {
            return today.get(Calendar.DAY_OF_WEEK) == report.getEmailDay();

        } else if (report.getEmailDelivery() == EmailDelivery.MONTHLY) {
            if (report.getEmailDay() == Report.LAST_DAY_OF_MONTH) {
                return today.get(Calendar.DATE) == today
                    .getActualMaximum(Calendar.DATE);
            } else {
                return today.get(Calendar.DATE) == report.getEmailDay();
            }
        }
        return false;
    }

    public static String frequencyString(ResourceBundle messages,
        EmailDelivery frequency) {
        if (frequency == EmailDelivery.WEEKLY) {
            return messages.getString("weekly");
        } else if (frequency == EmailDelivery.MONTHLY) {
            return messages.getString("monthly");
        } else {
            throw new IllegalArgumentException("Invalid frequency = "
                + frequency);
        }
    }

    public static String composeTextEmail(ReportSubscription sub, Report report) {

        // load our resource bundle with localized messages
        ResourceBundle mailMessages =
            ResourceBundle.getBundle(
                "org.activityinfo.server.mail.MailMessages",
                LocaleHelper.getLocaleObject(sub.getUser()));

        StringBuilder sb = new StringBuilder();

        String greeting = MessageFormat.format(
            mailMessages.getString("greeting"), sub.getUser().getName());
        sb.append(greeting).append("\n\n");

        String intro;
        if (sub.getInvitingUser() != null) {
            intro = MessageFormat.format(
                mailMessages.getString("reportIntroInvited"),
                sub.getInvitingUser().getName(),
                sub.getInvitingUser().getEmail(),
                report.getTitle(),
                frequencyString(mailMessages, sub.getEmailDelivery()));
        } else {
            intro = MessageFormat.format(mailMessages.getString("reportIntro"),
                report.getTitle(),
                frequencyString(mailMessages, sub.getEmailDelivery()));

        }
        sb.append(intro).append("\n\n");
        sb.append(MessageFormat.format(mailMessages.getString("viewLive"), sub
            .getTemplate().getId()));

        return sb.toString();
    }

    static String composeHtmlEmail(ReportSubscription sub, Report report) {

        // load our resource bundle with localized messages
        ResourceBundle mailMessages =
            ResourceBundle.getBundle(
                "org.activityinfo.server.mail.MailMessages",
                LocaleHelper.getLocaleObject(sub.getUser()));

        HtmlWriter htmlWriter = new HtmlWriter();

        htmlWriter.startDocument();
        htmlWriter.startDocumentBody();
        String greeting = MessageFormat.format(
            mailMessages.getString("greeting"), sub.getUser().getName());
        htmlWriter.paragraph(greeting);

        String intro;
        if (sub.getInvitingUser() != null) {
            intro = MessageFormat.format(
                mailMessages.getString("reportIntroInvited"),
                sub.getInvitingUser().getName(),
                sub.getInvitingUser().getEmail(),
                report.getTitle(),
                frequencyString(mailMessages, sub.getEmailDelivery()));
        } else {
            intro = MessageFormat.format(mailMessages.getString("reportIntro"),
                report.getTitle(),
                frequencyString(mailMessages, sub.getEmailDelivery()));

        }
        htmlWriter.paragraph(intro);

        htmlWriter.paragraph(MessageFormat.format(
            mailMessages.getString("viewLive"),
            Integer.toString(sub.getTemplate().getId())));

        htmlWriter.paragraph(mailMessages.getString("signature"));

        htmlWriter.endDocumentBody();
        htmlWriter.endDocument();

        return htmlWriter.toString();

    }
}
