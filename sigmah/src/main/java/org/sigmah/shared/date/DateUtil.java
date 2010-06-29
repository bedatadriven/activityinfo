/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.date;

import org.sigmah.shared.command.Month;
import org.sigmah.shared.report.content.DimensionCategory;
import org.sigmah.shared.report.content.MonthCategory;
import org.sigmah.shared.report.content.YearCategory;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.DateUnit;

import java.util.Date;

/**
 * Abstract class providing functions for manipulating dates. There are concrete implementations
 * based on the <code>Calendar</code> class, and another for client-side usage.
 *
 * @see org.sigmah.client.util.DateUtilGWTImpl
 * @see org.sigmah.server.util.DateUtilCalendarImpl
 *
 * @author Alex Bertram
 */
public abstract class DateUtil {


    public abstract Month getCurrentMonth();

    public abstract DateRange yearRange(int year);

    public abstract DateRange monthRange(int year, int month);

    public DateRange monthRange(Month month) {
        return monthRange(month.getYear(), month.getMonth());
    }

    public DateRange rangeFromCategory(DimensionCategory category) {

        if(category instanceof YearCategory) {
            return yearRange(((YearCategory)category).getYear());
        } else if(category instanceof MonthCategory) {
            MonthCategory monthCategory = (MonthCategory)category;
            return monthRange(monthCategory.getYear(), monthCategory.getMonth());
        } else {
            return new DateRange();
        }
    }


    public abstract int getYear(Date date);

    public abstract int getMonth(Date date);

    public abstract int getDay(Date date);

    public abstract Date floor(Date date, DateUnit dateUnit);

    public abstract Date ceil(Date date, DateUnit dateUnit);

    public abstract Date add(Date date, DateUnit dateUnit, int count);

    public abstract boolean isLastDayOfMonth(Date date);


    public Date startDateOfLastCompleteMonth(Date today) {
        // set this to the beginning of the last complete month
        Date start = floor(today, DateUnit.MONTH);
        if(!isLastDayOfMonth(today)) {
            start = add(start, DateUnit.MONTH, -1);
        }
        return start;
    }

    public Date endDateOfLastCompleteMonth(Date today) {
        // set this to the beginning of the last complete month
        Date end = ceil(today, DateUnit.MONTH);
        if(! isLastDayOfMonth(today)) {
            end = add(end, DateUnit.MONTH, -1);
        }
        return end;
    }

    public DateRange lastCompleteMonthRange(Date today) {
       return new DateRange(startDateOfLastCompleteMonth(today),
                            endDateOfLastCompleteMonth(today));
    }
}
