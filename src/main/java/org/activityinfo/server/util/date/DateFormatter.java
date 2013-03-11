package org.activityinfo.server.util.date;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.activityinfo.shared.report.model.DateRange;

public final class DateFormatter {
    private final DateFormat medium;
    private final DateFormat month;
    private final DateFormat monthYear;
    private final String rangePattern;
    private final String afterPattern;
    private final String beforePattern;

    public DateFormatter(Locale locale) {

        medium = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        month = getMonthFormat(locale);
        monthYear = getMonthYearFormat(locale);
        rangePattern = "%s - %s";
        beforePattern = "Activites termines jusqu'a %s"; // TODO i18n
        afterPattern = "Activites termines dans / apres %s";
    }

    public String format(DateRange range) {
        return format(range.getMinDate(), range.getMaxDate());
    }

    public String format(Date min, Date max) {

        if (min != null && max == null) {

            return String.format(afterPattern, medium.format(min));

        } else if (max != null && min == null) {

            return String.format(beforePattern, medium.format(max));

        } else if (min != null && max != null) {

            Calendar d1 = Calendar.getInstance();
            d1.setTime(min);

            Calendar d2 = Calendar.getInstance();
            d2.setTime(max);

            return format(d1, d2);

        } else {
            return null;
        }

    }

    private String format(Calendar min, Calendar max) {

        if (isMin(min, Calendar.MONTH) && isMin(min, Calendar.DATE) &&
            isMax(max, Calendar.MONTH) && isMax(max, Calendar.DATE)) {

            /* Case 1 - Range of years */

            if (min.get(Calendar.YEAR) == max.get(Calendar.YEAR)) {

                /* Case 1a - Single Year */

                return Integer.toString(min.get(Calendar.YEAR));

            } else {

                /* Case 1b - Multiple Years */

                return String.format(rangePattern,
                    Integer.toString(min.get(Calendar.YEAR)),
                    Integer.toString(max.get(Calendar.YEAR)));
            }
        } else if (isMin(min, Calendar.DATE) && isMax(max, Calendar.DATE)) {

            /* Case 2 - Range of months */

            if (min.get(Calendar.MONTH) == max.get(Calendar.MONTH) &&
                min.get(Calendar.YEAR) == max.get(Calendar.YEAR)) {

                /* Case 2a Single month */

                return monthYear.format(min.getTime());

            } else if (min.get(Calendar.YEAR) == max.get(Calendar.YEAR)) {

                /* Case 2b Multiple months in same year */

                return String.format(rangePattern,
                    month.format(min.getTime()),
                    monthYear.format(max.getTime()));

            } else {

                /* Case 3b multiple months over multiple years */

                return String.format(rangePattern,
                    monthYear.format(min.getTime()),
                    monthYear.format(max.getTime()));
            }

        } else {

            return String.format(rangePattern,
                medium.format(min.getTime()),
                medium.format(max.getTime()));
        }

    }

    private boolean isMax(Calendar c, int field) {
        return c.get(field) == c.getMaximum(field);
    }

    private boolean isMin(Calendar c, int field) {
        return c.get(field) == c.getMinimum(field);
    }

    private DateFormat getMonthYearFormat(Locale locale) {
        SimpleDateFormat format = (SimpleDateFormat) DateFormat
            .getDateInstance(DateFormat.SHORT, locale);
        format.applyPattern("MMM yyyy");

        return format;
    }

    private DateFormat getMonthFormat(Locale locale) {
        SimpleDateFormat format = (SimpleDateFormat) DateFormat
            .getDateInstance(DateFormat.SHORT, locale);
        format.applyPattern("MMM");

        return format;
    }

    public static String formatDateTime(Date date) {
        Date toFormat = date == null ? new Date() : date;
        return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(toFormat);
    }

    public static String formatDateTime(Long millis) {
        Date date = millis == null ? new Date() : new Date(millis);
        return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(date);
    }
}
