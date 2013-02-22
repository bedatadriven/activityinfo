

package org.activityinfo.server.command.handler.sync;

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

import java.sql.Timestamp;
import java.util.Date;

public class TimestampHelper {

    public static String toString(long timestamp) {
        return Long.toString(timestamp);
    }

    public static long fromString(String s) {
        if(s == null) {
            return 0;
        }
        String[] parts = s.split("\\.");
        return Long.parseLong(parts[0]);
    }
    
    public static Timestamp fromDate(Date d) {
    	if(d instanceof Timestamp) {
    		return (Timestamp)d;
    	}
    	return new Timestamp(d.getTime());
    }

    public static String toString(Date date) {
        if(date instanceof Timestamp) {
            return toString(date);
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
