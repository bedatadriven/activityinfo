/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.content;

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
