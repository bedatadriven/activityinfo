package org.activityinfo.shared.command;

import java.io.Serializable;

/**
 * Encapsulates a Gregorian month
 *
 * @author Alex Bertram
 */
public class Month implements Serializable, Comparable<Month> {

    int year;
    int month;

    /**
     * Constructs an uninitialized <code>Month</code>
     */
    public Month() {
    }

    /**
     * Constructs a <code>Month</code> at the given month and year.
     * If month is outside of the range 1..12, the values are normalized.
     * For example, <code>Month(2001, 13)</code> initalizes this <code>Month</code>
     * to January, 2002.
     *
     * @param year Gregorian year (for example, 1999, 2009)
     * @param month Gregorian month (january=1, feb=2, ... dec=12)
     */
    public Month(int year, int month) {
        this.year = year;
        this.month = month;

        while(month > 12) {
            year++;
            month -= 12;
        }
        while(month < 1) {
            year--;
            month+=12;
        }
    }

    /**
     * Gets this <code>Month</code>'s year
     *
     * @return the month's year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets this <code>Month</code>'s year.
     *
     * @param year Gregorian year (for example, 1999, 2009)
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets this month
     *
     * @return The Gregorian month (1=january, 12=december)
     */
    public int getMonth() {
        return month;
    }

    /**
     * Sets this month
     *
     * @param month The Gregorian month (1=january, 12=december)
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Compares this <code>Month</code> to another month
     *
     * @param m The <code>Month</code> with which to compare this <code>Month</code>
     * @return  0 if this month is the same as <code>m</code>, -1 if this month is
     * earlier than <code>m</code>, or +1 if this month follows <code>m</code>
     */
    public int compareTo(Month m) {
        if(year < m.year)
            return -1;
        if(year > m.year)
            return 1;
        if(month < m.month)
            return -1;
        if(month > m.month)
            return 1;
        return 0;
    }

    /**
     * Returns a string representation of this Month in
     * the format 2009-1
     *
     * @return a string representation of this Month in the format 2009-1, 2009-12
     */
    @Override
    public String toString() {
        return year + "-" + month;
    }

    /**
     * Parses a string in the format 2009-1, 2009-03, 2009-12 and returns
     * the value as a <code>Month</code>
     *
     * @param s The <code>String</code> to parse
     * @return  The value of the string as a <code>Month</code>
     */
    public static Month parseMonth(String s) {
        String[] tokens = s.split("-");
        if(tokens.length != 2)
            throw new NumberFormatException();

        return new Month(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Month month1 = (Month) o;

        if (month != month1.month) return false;
        if (year != month1.year) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        return result;
    }

    public Month plus(int count) {
        return new Month(year, month+count);
    }

    public Month next() {
        return plus(+1);
    }

    public Month previous() {
        return plus(-1);
    }
}
