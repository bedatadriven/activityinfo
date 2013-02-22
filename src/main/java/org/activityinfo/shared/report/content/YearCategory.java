

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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        YearCategory that = (YearCategory) o;

        if (year != that.year) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return year;
    }

    @Override
    public String toString() {
        return "YearCategory{" + year + '}';
    }

	@Override
	public String getLabel() {
		return Integer.toString(year);
	}
}
