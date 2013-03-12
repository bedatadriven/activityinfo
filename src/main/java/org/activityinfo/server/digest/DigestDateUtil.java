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

    public static long midnightMillisDaysAgo(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -days);
        midnight(c);
        return c.getTimeInMillis();
    }

    public static void midnight(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }
}
