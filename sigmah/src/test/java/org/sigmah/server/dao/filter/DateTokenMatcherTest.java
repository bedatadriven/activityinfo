package org.sigmah.server.dao.filter;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.shared.report.model.DateRange;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class DateTokenMatcherTest {


    @Test
    public void testYear() {

        DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
        DateRange r = matcher.tryParse("2007");

        assertDate("date1", r.getMinDate(), 2007, 1, 1);
        assertDate("date2", r.getMaxDate(), 2007, 12, 31);
    }

    @Test
    public void testBeforeYear() {

        DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
        DateRange r = matcher.tryParse("avant 2009");

        Assert.assertNull("date1", r.getMinDate());
        assertDate("date2", r.getMaxDate(), 2008, 12, 31);
    }

    @Test
    public void testAfterYear() {

        DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
        DateRange r = matcher.tryParse("apres 2008");

        assertDate("date1", r.getMinDate(), 2009, 1, 1);
        Assert.assertNull("date2", r.getMaxDate());
    }

    @Test
    public void testSingleMonthRange() {
        DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
        DateRange r = matcher.tryParse("janvier 2009");

        assertDate("date1", r.getMinDate(), 2009, 1, 1);
        assertDate("date2", r.getMaxDate(), 2009, 1, 31);
    }

    @Test
    public void testMonthRangeImplicit() {
        DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
        matcher.setToday(2009, 8, 7);
        DateRange r = matcher.tryParse("avril");

        assertDate("date1", r.getMinDate(), 2009, 4, 1);
        assertDate("date2", r.getMaxDate(), 2009, 4, 30);
    }

    @Test
    public void testAfterMonth() {
        DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
        DateRange r = matcher.tryParse("apres juin 2009");

        assertDate("date1", r.getMinDate(), 2009, 7, 1);
        Assert.assertNull("date2", null);
    }

    @Test
    public void testAfterMonthAtEndOfYear() {
        DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
        DateRange r = matcher.tryParse("apres dec 2009");

        assertDate("date1", r.getMinDate(), 2010, 1, 1);
        Assert.assertNull("date2", null);
    }

    @Test
    public void testBeforeMonth() {
          DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
          DateRange r = matcher.tryParse("avant mai 2009");

          Assert.assertNull("date1", null);
          assertDate("date1", r.getMaxDate(), 2009,4, 30);
    }

    @Test
    public void testAbsDate() {
        DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
        DateRange r = matcher.tryParse("13 mai 2009");

        assertDate("date1", r.getMinDate(), 2009, 5, 13);
        assertDate("date2", r.getMaxDate(), 2009, 5, 13);

    }

    @Test
    public void testAbsDate2() {
        DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
        DateRange r = matcher.tryParse("13/5/09");

        assertDate("date1", r.getMinDate(), 2009, 5, 13);
        assertDate("date2", r.getMaxDate(), 2009, 5, 13);
    }

    @Test
    public void testAfterAbsDate() {
        DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
        DateRange r = matcher.tryParse("apres 13/5/09");

        assertDate("date1", r.getMinDate(), 2009, 5, 14);
        Assert.assertNull("date2", r.getMaxDate());
    }
        

    @Test
    public void testYearRange() {
        DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
        DateRange r = matcher.tryParse("2007 - 2009");

        assertDate("date1", r.getMinDate(), 2007, 1, 1);
        assertDate("date2", r.getMaxDate(), 2009, 12, 31);
    }

    @Test
    public void testMonthRange() {
        DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
        DateRange r = matcher.tryParse("janvier 2007 - avril 2009");

        assertDate("date1", r.getMinDate(), 2007, 1, 1);
        assertDate("date2", r.getMaxDate(), 2009, 4, 30);
    }

     @Test
     public void testMonthRangeInSameYear() {
         DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
         DateRange r = matcher.tryParse("janvier - avril 2009");

         assertDate("date1", r.getMinDate(), 2009, 1, 1);
         assertDate("date2", r.getMaxDate(), 2009, 4, 30);
     }

    @Test
     public void testMonthRangeImplicitYear() {
         DateTokenMatcher matcher = new DateTokenMatcher(Locale.FRENCH);
            matcher.setToday(2008, 8, 7);
         DateRange r = matcher.tryParse("janvier - avril");

         assertDate("date1", r.getMinDate(), 2008, 1, 1);
         assertDate("date2", r.getMaxDate(), 2008, 4, 30);
     }


    public void assertDate(String name, Date date, int year, int month, int day) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int aYear = c.get(Calendar.YEAR);
        int aMonth = c.get(Calendar.MONTH)+1;
        int aDay = c.get(Calendar.DATE);

        if(year != aYear || month != aMonth || day != aDay) {
            Assert.fail(String.format("%s: Expected %d-%d-%d, actual %d-%d-%d", name, year, month, day, aYear, aMonth, aDay));
        }

    }
}
