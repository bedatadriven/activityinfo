/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.util;

import com.extjs.gxt.ui.client.util.DateWrapper;
import org.sigmah.shared.command.Month;
import org.sigmah.shared.date.DateUtil;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.DateUnit;

import java.util.Date;

/**
 * Client-side implementation of the {@link org.sigmah.shared.date.DateUtil} interface.
 *
 * @author Alex Bertram
 */
public class DateUtilGWTImpl extends DateUtil {

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
