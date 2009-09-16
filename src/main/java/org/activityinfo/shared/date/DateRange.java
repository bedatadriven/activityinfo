package org.activityinfo.shared.date;

import java.util.Date;
/*
 * @author Alex Bertram
 */

public class DateRange {
    public Date date1;
    public Date date2;

    public DateRange(Date date1, Date date2) {
        this.date1 = date1;
        this.date2 = date2;
    }

    public DateRange() {
        date1= null;
        date2 = null;
    }
}
