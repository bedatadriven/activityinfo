/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.util.date;

import java.util.Date;

import org.activityinfo.shared.command.Month;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.util.date.DateUtil;

import com.extjs.gxt.ui.client.util.DateWrapper;

/**
 * Client-side implementation of the {@link org.activityinfo.shared.util.date.DateUtil} interface.
 *
 * @author Alex Bertram
 */
public class DateUtilGWTImpl extends DateUtil {

	
	public static final DateUtilGWTImpl INSTANCE = new DateUtilGWTImpl(); 
	
    @Override
	public Month getCurrentMonth() {
        DateWrapper today = new DateWrapper();
        return new Month(today.getFullYear(), today.getMonth());
    }


    @Override
    public DateRange yearRange(int year) {
        DateRange range = new DateRange();

        DateWrapper date = new DateWrapper(year, 0, 1);
        range.setMinDate(date.asDate());

        date = new DateWrapper(year, 11, 31);
        range.setMaxDate(date.asDate());

        return range;

    }

    @Override
    public DateRange monthRange(int year, int month) {

        DateRange range = new DateRange();

        DateWrapper date = new DateWrapper(year, month-1, 1);
        range.setMinDate(date.asDate());

        date = date.addMonths(1);
        date = date.addDays(-1);
        range.setMaxDate(date.asDate());

        return range;

    }


    @Override
    public int getYear(Date date) {
        DateWrapper dw = new DateWrapper(date);
        return dw.getFullYear();
    }

    @Override
    public int getMonth(Date date) {
        DateWrapper dw = new DateWrapper(date);
        return dw.getMonth()+1;
    }

    @Override
    public int getDay(Date date) {
        DateWrapper dw = new DateWrapper(date);
        return dw.getDay();
    }

    @Override
    public Date floor(Date date, DateUnit dateUnit) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Date ceil(Date date, DateUnit dateUnit) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Date add(Date date, DateUnit dateUnit, int count) {
         throw new RuntimeException("not implemented");
    }

    @Override
    public boolean isLastDayOfMonth(Date date) {
        return false;
    }
}
