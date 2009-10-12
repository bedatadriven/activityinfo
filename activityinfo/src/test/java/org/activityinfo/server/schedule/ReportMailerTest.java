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

package org.activityinfo.server.schedule;

import org.junit.Test;
import org.junit.Assert;
import org.activityinfo.server.domain.ReportSubscription;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.ReportTemplate;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.ParameterizedValue;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.ReportFrequency;
import org.activityinfo.shared.date.DateRange;

import java.util.Date;
import java.util.Calendar;
import java.util.Map;

/**
 *
 *
 * @author Alex Bertram
 */
public class ReportMailerTest {

    @Test
    public void testDailyCheck() {

        Report sub = new Report();
        sub.setFrequency(ReportFrequency.DAILY);

        Assert.assertTrue("Daily report always goes out", ReportMailerHelper.mailToday(new Date(), sub));

    }

    @Test
    public void testWeeklyCheck() {

        Report report = new Report();
        report.setFrequency(ReportFrequency.WEEKLY);
        report.setDay(0); // Sunday

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2009);
        cal.set(Calendar.MONTH, 9);
        cal.set(Calendar.DATE, 4);

        Assert.assertTrue("Sunday report goes out on Sunday", ReportMailerHelper.mailToday(cal.getTime(), report));

        cal.set(Calendar.DATE, 5);

        Assert.assertFalse("Sunday report does not out on Monday", ReportMailerHelper.mailToday(cal.getTime(), report));

        report.setDay(1);

        Assert.assertTrue("Monday report goes out on Monday", ReportMailerHelper.mailToday(cal.getTime(), report));
    }

    @Test
    public void testMonthly() {

        Report report = new Report();
        report.setFrequency(ReportFrequency.MONTHLY);
        report.setDay(11);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2009);
        cal.set(Calendar.MONTH, 3);
        cal.set(Calendar.DATE, 11);

        Assert.assertTrue("Monthly report scheduled for each the 11th goes out on the 11th",
                ReportMailerHelper.mailToday(cal.getTime(), report));



        cal.set(Calendar.DATE, 30);
    }

    @Test
    public void testLastDayOfMonth() {

        Report report = new Report();
        report.setFrequency(ReportFrequency.MONTHLY);
        report.setDay(ReportFrequency.LAST_DAY_OF_MONTH);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2009);
        cal.set(Calendar.MONTH, 3);
        cal.set(Calendar.DATE, 30);

        Assert.assertTrue("Report goes out on 4-April",
                ReportMailerHelper.mailToday(cal.getTime(), report));

        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DATE, 31);

        Assert.assertTrue("Report goes out on 31-Jan",
                ReportMailerHelper.mailToday(cal.getTime(), report));

    }

    @Test
    public void testDateParameters() {

        Report report = new Report();
        report.setFrequency(ReportFrequency.MONTHLY);


        Calendar today = Calendar.getInstance();
        today.set(Calendar.DATE, 5);
        today.set(Calendar.MONTH, 0);
        today.set(Calendar.YEAR, 2009);

        DateRange dateRange = ReportMailerHelper.computeDateRange(report, today.getTime() );

        Calendar month = Calendar.getInstance();
        month.setTime(dateRange.getMaxDate());

        Assert.assertEquals(11, month.get(Calendar.MONTH));
        Assert.assertEquals(31, month.get(Calendar.DATE));
        Assert.assertEquals(2008, month.get(Calendar.YEAR));

    }

    @Test
    public void testTextEmail() {

        User user = new User("akbertram@gmail.com", "Alex", "fr");

        ReportSubscription sub = new ReportSubscription();
        sub.setTemplate(new ReportTemplate());
        sub.getTemplate().setId(1);
        sub.setUser(user);

        Report report = new Report();
        report.setTitle("Rapport RRM Mensuelle");
        report.setFrequency(ReportFrequency.WEEKLY);
        report.setDay(1);

        String text = ReportMailerHelper.composeTextEmail(sub, report);
        
        System.out.println(text);

        Assert.assertTrue("user name is present", text.contains(user.getName()));

    }

}
