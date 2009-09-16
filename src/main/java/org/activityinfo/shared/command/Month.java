package org.activityinfo.shared.command;

import java.io.Serializable;
/*
 * @author Alex Bertram
 */

public class Month implements Serializable, Comparable<Month> {

    int year;
    int month;

    public Month() {
    }

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

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

    @Override
    public String toString() {
        return year + "-" + month;
    }

    public static Month parseMonth(String s) {
        String[] tokens = s.split("-");
        if(tokens.length != 2)
            throw new NumberFormatException();

        return new Month(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
    }

    public Month next() {
        return new Month(year, month+1);
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
}
