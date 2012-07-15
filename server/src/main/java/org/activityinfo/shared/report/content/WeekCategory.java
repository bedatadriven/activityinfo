package org.activityinfo.shared.report.content;

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
