package org.activityinfo.server.schedule;

import org.activityinfo.server.domain.ReportSubscription;
import org.activityinfo.server.util.DateUtilCalendarImpl;
import org.activityinfo.shared.domain.Subscription;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.ParameterizedFilter;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.ParameterizedValue;
import org.activityinfo.shared.date.DateUtil;

import java.util.Date;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
/*
 * @author Alex Bertram
 */

public class ReportMailerHelper {

    private static final DateUtil dateUtil = new DateUtilCalendarImpl();


    public static boolean mailToday(Date dateToday, ReportSubscription sub) {

        Calendar today = Calendar.getInstance();
        today.setTime(dateToday);

        if(sub.getFrequency() == Subscription.DAILY) {
            return true;
        } else if(sub.getFrequency() == Subscription.WEEKLY) {
            return today.get(Calendar.DAY_OF_WEEK) == sub.getDay();
        } else if(sub.getFrequency() == Subscription.MONTHLY) {
            if(sub.getDay() == Subscription.LAST_DAY_OF_MONTH) {
                return today.get(Calendar.DATE) == today.getActualMaximum(Calendar.DATE);
            } else {
                return today.get(Calendar.DATE) == sub.getDay();
            }
        }
        return false;
    }

    public static Map<String,Object> computeDateParameters(Report report, ReportSubscription sub, Date today) {

        Map<String,Object> paramValues = new HashMap();
        ParameterizedFilter filter = report.getFilter();
        if(filter.getMinDate().getParameterName() != null) {
            paramValues.put(filter.getMinDate().getParameterName(), computeMinDate(filter, sub, today));
        }
        if(filter.getMaxDate().getParameterName() != null) {
            paramValues.put(filter.getMaxDate().getParameterName(), computeMaxDate(filter, sub, today));
        }

        return paramValues;

    }

    private static Date computeMinDate(ParameterizedFilter filter, ReportSubscription sub, Date today) {


        if(filter.getDateUnit() == DateUnit.YEAR) {

            // set this to beginning of current year
            return  dateUtil.floor(today, DateUnit.YEAR);

        } else if(filter.getDateUnit() == DateUnit.QUARTER) {

            throw new RuntimeException("Quarter not implemened");

        } else if(filter.getDateUnit() == DateUnit.MONTH) {

            return startDateOfLastCompleteMonth(today);

        } else if(filter.getDateUnit() == DateUnit.WEEK) {
            // set this to 7 days ago
            return dateUtil.add(today, DateUnit.DAY, -7);

        } else if(filter.getDateUnit() == DateUnit.DAY) {

            // set this to based on the subscription frequency
            if(sub.getFrequency() == Subscription.DAILY) {
                return dateUtil.add(today, DateUnit.DAY, -1);
            } else if(sub.getFrequency() == Subscription.WEEKLY) {
                return dateUtil.add(today, DateUnit.DAY, -7);
            } else if(sub.getFrequency() == Subscription.MONTHLY) {
                return startDateOfLastCompleteMonth(today);
            } else {
                throw new RuntimeException("Invalid subscription frequency value = " + sub.getFrequency());
            }
        } else {
            throw new RuntimeException("Invalid date unit = " + filter.getDateUnit().toString());
        }
    }

    private static Date computeMaxDate(ParameterizedFilter filter, ReportSubscription sub, Date today) {
        if(filter.getDateUnit() == DateUnit.YEAR) {
            return dateUtil.ceil(today, DateUnit.YEAR);

        } else if(filter.getDateUnit() == DateUnit.QUARTER) {
            throw new RuntimeException("oops logic for quarter not implemented");

        } else if(filter.getDateUnit() == DateUnit.MONTH) {
            return endDateOfLastCompleteMonth(today);
        } else {
            return today;
        }
    }


    private static Date startDateOfLastCompleteMonth(Date today) {
        // set this to the beginning of the last complete month
        Date start = dateUtil.floor(today, DateUnit.MONTH);
        if(!dateUtil.isLastDayOfMonth(today)) {
            start = dateUtil.add(start, DateUnit.MONTH, -1);
        }
        return start;
    }

    private static Date endDateOfLastCompleteMonth(Date today) {
        // set this to the beginning of the last complete month
        Date end = dateUtil.ceil(today, DateUnit.MONTH);
        if(!dateUtil.isLastDayOfMonth(today)) {
            end = dateUtil.add(end, DateUnit.MONTH, -1);
        }
        return end;
    }

}
