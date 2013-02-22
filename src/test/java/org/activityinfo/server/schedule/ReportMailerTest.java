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

import java.util.Calendar;

import org.activityinfo.server.database.hibernate.entity.ReportDefinition;
import org.activityinfo.server.database.hibernate.entity.ReportSubscription;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.report.model.EmailDelivery;
import org.activityinfo.shared.report.model.Report;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alex Bertram
 */
public class ReportMailerTest {

    @Test
    public void testWeeklyCheck() {

        ReportSubscription report = new ReportSubscription();
        report.setEmailDelivery(EmailDelivery.WEEKLY);
        report.setEmailDay(7); // Saturday

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, Calendar.AUGUST);
        cal.set(Calendar.DATE, 11);

        Assert.assertTrue("Saturday report goes out on Saturday",
            ReportMailerHelper.mailToday(cal.getTime(), report));

        cal.set(Calendar.DATE, 5);

        Assert.assertFalse("Sunday report does not out on Monday",
            ReportMailerHelper.mailToday(cal.getTime(), report));

        report.setEmailDay(1);

        Assert.assertTrue("Monday report goes out on Monday",
            ReportMailerHelper.mailToday(cal.getTime(), report));
    }

    @Test
    public void testMonthly() {

        ReportSubscription report = new ReportSubscription();
        report.setEmailDelivery(EmailDelivery.MONTHLY);
        report.setEmailDay(11);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2009);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 11);

        Assert.assertTrue(
            "Monthly report scheduled for each the 11th goes out on the 11th",
            ReportMailerHelper.mailToday(cal.getTime(), report));

        cal.set(Calendar.DATE, 30);
    }

    @Test
    public void testLastDayOfMonth() {

        ReportSubscription report = new ReportSubscription();
        report.setEmailDelivery(EmailDelivery.MONTHLY);
        report.setEmailDay(Report.LAST_DAY_OF_MONTH);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2009);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DATE, 30);

        Assert.assertTrue("Report goes out on 4-April",
            ReportMailerHelper.mailToday(cal.getTime(), report));

        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 31);

        Assert.assertTrue("Report goes out on 31-Jan",
            ReportMailerHelper.mailToday(cal.getTime(), report));

    }

    @Test
    public void testTextEmail() {
        testEmail("en");
    }

    @Test
    public void testFrenchTextEmail() {
        testEmail("fr");
    }

    private void testEmail(String locale) {
        User user = new User();
        user.setEmail("akbertram@gmail.com");
        user.setName("alex");
        user.setLocale(locale);

        ReportSubscription sub = new ReportSubscription();
        sub.setTemplate(new ReportDefinition());
        sub.getTemplate().setId(5040);
        sub.setUser(user);
        sub.setEmailDelivery(EmailDelivery.WEEKLY);
        sub.setEmailDay(1);

        Report report = new Report();
        report.setTitle("Rapport RRM Mensuelle");

        String text = ReportMailerHelper.composeTextEmail(sub, report);

        System.out.println(text);

        Assert
            .assertTrue("user name is present", text.contains(user.getName()));
        Assert.assertTrue("link is correct without comma",
            text.contains("#report/5040"));
    }

}
