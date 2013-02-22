

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

import java.util.Date;

import org.activityinfo.client.i18n.I18N;

/**
 * DimensionCategory within the Month dimension. Each category
 * represents a calendar month.
 */
public class MonthCategory implements DimensionCategory {

    private int year;
    private int month;

    /**
     * Required for GWT serialization
     */
    private MonthCategory() {

    }

    public MonthCategory(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    @Override
    public Comparable getSortKey() {
        return hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MonthCategory that = (MonthCategory) o;

        if (month != that.month) {
            return false;
        }
        if (year != that.year) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return year * 100 + month;
    }

    @Override
    public String toString() {
        return "MonthCategory{" + year + "/" + month + "}";
    }
   
	@Override
	@SuppressWarnings("deprecation")
	public String getLabel() {
    	// hackish, yes, but it's the only thing that works on client & server
		Date date = new Date(year-1900, month-1, 1);
		return I18N.MESSAGES.month(date);
	}
}
