/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.schedule;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.EmailDelivery;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.util.date.DateUtil;
import org.sigmah.server.database.hibernate.entity.ReportSubscription;
import org.sigmah.server.i18n.LocaleHelper;
import org.sigmah.server.util.date.DateUtilCalendarImpl;
import org.sigmah.server.util.html.HtmlWriter;

/*
 * @author Alex Bertram
 */
public class ReportMailerHelper {

    private static final DateUtil DATE_UTIL = new DateUtilCalendarImpl();


    /**
     * Checks if the given ReportSubscription should be mailed today.
     *
     * @param dateToday Today's date
     * @param report The report for which to check
     * @return True if the report should be mailed.
     */
    public static boolean mailToday(Date dateToday, ReportSubscription report) {

        Calendar today = Calendar.getInstance();
        today.setTime(dateToday);

        if(report.getEmailDelivery() == EmailDelivery.WEEKLY) {
            return today.get(Calendar.DAY_OF_WEEK) == report.getEmailDay()+1;

        } else if(report.getEmailDelivery() == EmailDelivery.MONTHLY) {
            if(report.getEmailDay() == Report.LAST_DAY_OF_MONTH) {
                return today.get(Calendar.DATE) == today.getActualMaximum(Calendar.DATE);
            } else {
                return today.get(Calendar.DATE) == report.getEmailDay();
            }
        }
        return false;
    }

    public static String frequencyString(ResourceBundle messages, EmailDelivery frequency) {
        if(frequency == EmailDelivery.WEEKLY) {
            return messages.getString("weekly");
        } else if(frequency == EmailDelivery.MONTHLY) {
            return messages.getString("monthly");
        } else {
            throw new IllegalArgumentException("Invalid frequency = " + frequency);
        }
    }

    public static String composeTextEmail(ReportSubscription sub, Report report) {

        // load our resource bundle with localized messages
        ResourceBundle mailMessages =
              ResourceBundle.getBundle("org.activityinfo.server.mail.MailMessages",  LocaleHelper.getLocaleObject(sub.getUser()));

        StringBuilder sb = new StringBuilder();

        String greeting = MessageFormat.format(mailMessages.getString("greeting"), sub.getUser().getName());
        sb.append(greeting).append("\n\n");

        String intro;
        if(sub.getInvitingUser() != null) {
            intro = MessageFormat.format(mailMessages.getString("reportIntroInvited"),
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
        sb.append(MessageFormat.format(mailMessages.getString("viewLive"), sub.getTemplate().getId()));

        return sb.toString();
    }

    static String composeHtmlEmail(ReportSubscription sub, Report report) {

        // load our resource bundle with localized messages
        ResourceBundle mailMessages =
              ResourceBundle.getBundle("org.activityinfo.server.mail.MailMessages", LocaleHelper.getLocaleObject(sub.getUser()));

        HtmlWriter htmlWriter = new HtmlWriter();

        htmlWriter.startDocument();
        htmlWriter.startDocumentBody();
        String greeting = MessageFormat.format(mailMessages.getString("greeting"), sub.getUser().getName());
        htmlWriter.paragraph(greeting);

        String intro;
        if(sub.getInvitingUser() != null) {
            intro = MessageFormat.format(mailMessages.getString("reportIntroInvited"),
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

        htmlWriter.paragraph(MessageFormat.format(mailMessages.getString("viewLive"),
                Integer.toString(sub.getTemplate().getId())));

        htmlWriter.paragraph(mailMessages.getString("signature"));

        htmlWriter.endDocumentBody();
        htmlWriter.endDocument();

        return htmlWriter.toString();

    }
}
