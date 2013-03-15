package org.activityinfo.server.digest;

import java.util.Calendar;
import java.util.Date;

public class DigestDateUtil {

    public static boolean isOn(Date date, long compare) {
        return daysBetween(date, compare) == 0;
    }

    public static int daysBetween(Date date, long oldDateMillis) {
        long diff = nextMidnightMillis(date) - oldDateMillis;
        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    public static long nextMidnightMillis(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        midnight(c);
        return c.getTimeInMillis();
    }

    public static Date daysAgo(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (days > 0) {
            c.add(Calendar.DATE, -days);
        }
        return c.getTime();
    }

    public static long millisDaysAgo(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (days > 0) {
            c.add(Calendar.DATE, -days);
        }
        return c.getTimeInMillis();
    }

    public static long midnightMillisDaysAgo(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (days > 0) {
            c.add(Calendar.DATE, -days);
        }
        midnight(c);
        return c.getTimeInMillis();
    }

    /**
     * Resets the time component of the specified calendar to zero: midnight at the <b>start</b> of the day.
     * 
     * @param c
     */
    public static void midnight(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }
}
