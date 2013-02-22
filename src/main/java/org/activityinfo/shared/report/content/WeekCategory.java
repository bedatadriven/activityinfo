package org.activityinfo.shared.report.content;

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

import org.activityinfo.client.i18n.I18N;

public class WeekCategory  implements DimensionCategory {

    private static final int MAX_WEEKS_PER_YEAR = 60; // just to be safe
    
	private int year;
    private int week;

    public WeekCategory() {
    }

    public WeekCategory(int year, int quarter) {
        this.year = year;
        this.week = quarter;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public Comparable getSortKey() {
        return year * MAX_WEEKS_PER_YEAR + week;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WeekCategory that = (WeekCategory) o;

        if (week != that.week) {
            return false;
        }
        if (year != that.year) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + week;
        return result;
    }

    @Override
    public String toString() {
        return "WeekCategory{" + year + " W " + week + "}";
    }

	@Override
	public String getLabel() {
		return I18N.MESSAGES.week(year, week);
	}
}
