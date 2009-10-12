package org.activityinfo.server.schedule;

import org.activityinfo.server.domain.ReportSubscription;
import org.activityinfo.server.util.DateUtilCalendarImpl;
import org.activityinfo.server.report.util.HtmlWriter;
import org.activityinfo.shared.date.DateUtil;
import org.activityinfo.shared.date.DateRange;
import org.activityinfo.shared.report.model.*;

import java.util.*;
import java.text.MessageFormat;

/*
 * @author Alex Bertram
 */
public class ReportMailerHelper {

    private static final DateUtil dateUtil = new DateUtilCalendarImpl();


    /**
     * Checks if the given ReportSubscription should be mailed today.
     *
     * @param dateToday Today's date
     * @param report The report for which to check
     * @return True if the report should be mailed.
     */
    public static boolean mailToday(Date dateToday, Report report) {

        Calendar today = Calendar.getInstance();
        today.setTime(dateToday);

        if(report.getFrequency() == ReportFrequency.DAILY) {
            return true;

        } else if(report.getFrequency() == ReportFrequency.WEEKLY) {
            return today.get(Calendar.DAY_OF_WEEK) == report.getDay()+1;

        } else if(report.getFrequency() == ReportFrequency.MONTHLY) {
            if(report.getDay() == ReportFrequency.LAST_DAY_OF_MONTH) {
                return today.get(Calendar.DATE) == today.getActualMaximum(Calendar.DATE);
            } else {
                return today.get(Calendar.DATE) == report.getDay();
            }
        }
        return false;
    }

    public static DateRange computeDateRange(Report report, Date today) {

        if(report.getFrequency() == ReportFrequency.MONTHLY) {
            return dateUtil.lastCompleteMonthRange(today);

        } else if(report.getFrequency() == ReportFrequency.WEEKLY) {
            DateRange lastWeek = new DateRange();
            lastWeek.setMaxDate(today);
            lastWeek.setMinDate(dateUtil.add(today, DateUnit.WEEK, -1));

            return lastWeek;

        } else {
            return new DateRange();
        }
    }



    public static String frequencyString(ResourceBundle messages, int frequency) {
        if(frequency == ReportFrequency.DAILY) {
            return messages.getString("daily");
        } else if(frequency == ReportFrequency.WEEKLY) {
            return messages.getString("weekly");
        } else if(frequency == ReportFrequency.MONTHLY) {
            return messages.getString("monthly");
        } else {
            throw new RuntimeException("Invalid frequency = " + frequency);
        }
    }

    public static String composeTextEmail(ReportSubscription sub, Report report) {

        // load our resource bundle with localized messages
        ResourceBundle mailMessages =
              ResourceBundle.getBundle("org.activityinfo.server.mail.MailMessages", sub.getUser().getLocaleObject());

        StringBuilder sb = new StringBuilder();

        String greeting = MessageFormat.format(mailMessages.getString("greeting"), sub.getUser().getName());
        sb.append(greeting).append("\n\n");

        String intro;
        if(sub.getInvitingUser() != null) {
            intro = MessageFormat.format(mailMessages.getString("reportIntroInvited"),
                    sub.getInvitingUser().getName(),
                    sub.getInvitingUser().getEmail(),
                    report.getTitle(),
                    frequencyString(mailMessages, report.getFrequency()));
        } else {
            intro = MessageFormat.format(mailMessages.getString("reportIntro"),
                    report.getTitle(),
                    frequencyString(mailMessages, report.getFrequency()));

        }
        sb.append(intro).append("\n\n");
        sb.append(MessageFormat.format(mailMessages.getString("viewLive"), sub.getTemplate().getId()));

        return sb.toString();
    }

    static String composeHtmlEmail(ReportSubscription sub, Report report) {

        // load our resource bundle with localized messages
        ResourceBundle mailMessages =
              ResourceBundle.getBundle("org.activityinfo.server.mail.MailMessages", sub.getUser().getLocaleObject());

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
                    frequencyString(mailMessages, report.getFrequency()));
        } else {
            intro = MessageFormat.format(mailMessages.getString("reportIntro"),
                    report.getTitle(),
                    frequencyString(mailMessages, report.getFrequency()));

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
