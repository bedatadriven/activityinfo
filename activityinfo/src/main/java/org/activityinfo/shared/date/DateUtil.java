package org.activityinfo.shared.date;

import org.activityinfo.shared.command.Month;
import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.content.MonthCategory;
import org.activityinfo.shared.report.content.YearCategory;
import org.activityinfo.shared.report.model.DateUnit;

import java.util.Date;
/*
 * @author Alex Bertram
 */

public abstract class DateUtil {


    public abstract Month getCurrentMonth();

    public abstract DateRange yearRange(int year);

    public abstract DateRange monthRange(int year, int month);

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

}
