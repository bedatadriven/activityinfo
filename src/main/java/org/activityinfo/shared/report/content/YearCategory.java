package org.activityinfo.shared.report.content;

import org.activityinfo.shared.report.content.DimensionCategory;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class YearCategory implements DimensionCategory {

    private int year;

    /**
     * Required for GWT Serialization
     */
    private YearCategory() {

    }

    public YearCategory(int year) {
        this.year = year;
    }

    @Override
    public Comparable getSortKey() {
        return year;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YearCategory that = (YearCategory) o;

        if (year != that.year) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return year;
    }
}
