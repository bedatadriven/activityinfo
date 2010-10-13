/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.content;

/**
 * @author Alex Bertram (akbertram@gmail.com)
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
}
