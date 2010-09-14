/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.sync;

import java.sql.Timestamp;
import java.util.Date;

public class TimestampHelper {

    public static String toString(Timestamp timestamp) {
        return timestamp.getTime() + "." + timestamp.getNanos();
    }

    public static Timestamp fromString(String s) {
        if(s == null) {
            return new Timestamp(0);
        }
        String[] parts = s.split("\\.");
        Timestamp timestamp = new Timestamp(Long.parseLong(parts[0]));
        if(parts.length == 2) {
            timestamp.setNanos(Integer.parseInt(parts[1]));
        }
        return timestamp;
    }

    public static String toString(Date date) {
        if(date instanceof Timestamp) {
            return toString((Timestamp)date);
        } else {
            return Long.toString(date.getTime());
        }

    }

    static boolean isAfter(Date date1, Date date2) {
        if(date1.after(date2)) {
            return true;
        }
        if(date1.before(date2)) {
            return false;
        }

        if(date1 instanceof Timestamp && date2 instanceof Timestamp) {
            int nano1 = ((Timestamp) date1).getNanos();
            int nano2 = ((Timestamp) date2).getNanos();
            return nano1 > nano2;
        }
        return false;
    }
}
