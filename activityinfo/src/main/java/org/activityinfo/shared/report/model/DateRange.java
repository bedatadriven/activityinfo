package org.activityinfo.shared.report.model;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * Defines a time period as a range of dates.
 *
 * The end points of the range are <em>inclusive</em>, so that a date range of
 * 1-Jan-09 to 31-Jan-09 would include all events that took place at any moment in
 * the month of January.
 *
 * The <code>DateRange</code> can be also be open on either end.
 *
 * Here are a few concrete examples:
 *
 * <table>
 * <thead>
 * <tr>
 *      <td><strong><code>minDate</code></strong></td>
 *      <td><strong><code>maxDate</code></strong></td>
 *      <td><strong>Meaning</strong></td></tr>
 * </thead>
 * <tbody>
 * <tr>
 *      <td><code>null</code>
 *      </td><td><code>null</code></td>
 *      <td>All dates</td>
 * </tr>
 * <tr>
 *      <td>1-Feb-09</td>
 *      <td><code>null</code></td>
 *      <td>All dates on or after 1-Feb-09</td>
 * </tr>
 * <tr>
 *      <td><code>null</code></td>
 *      <td>31-Jan-09</td>
 *      <td>All dates on or before 31-Jan-09 (2009 or earlier)</td>
 * </tr>
 * </tbody>
 * </table>
 *
 *
 * @author Alex Bertram
 */
public class DateRange implements Serializable {


    private Date minDate;

    private Date maxDate;

    /**
     * Constructs a <code>DateRange</code> bounded by <code>minDate</code> and <code>maxDate</code>
     *
     * @param minDate The minimum date to be included in this range (inclusive), or <code>null</code> if there
     *                is no minimum bound
     * @param maxDate The maximum date to be included in this range (inclusive), or <code>null</code> if there
     *                is no maximum bound.
     */
    public DateRange(Date minDate, Date maxDate) {
        this.setMinDate(minDate);
        this.setMaxDate(maxDate);
    }

    /**
     * Constructs a fully open date range (all dates are included).
     */
    public DateRange() {
        setMinDate(null);
        setMaxDate(null);
    }


    /**
     * Gets the minimum date in this range (inclusive).
     *
     * @return The minimum date in this range (inclusive) or <code>null</code> if the
     * range has no lower bound
     */
    @XmlAttribute(name="min")
    public Date getMinDate() {
        return minDate;
    }

    /**
     * Sets the minimum date in this range (inclusive).
     *
     * @param minDate The minimum date in this range (inclusive) or <code>null</code> if the range has
     * now upper bound.
     */
    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    /**
     * Gets the maximum date in this range (inclusive).
     *
     * @return The maximum date in this range (inclusive) or <code>null</code> if the range has no upper bound.
     */
    @XmlAttribute(name="max")
    public Date getMaxDate() {
        return maxDate;
    }

    /**
     * Sets the maximum date in this range (inclusive).
     *
     * @param maxDate The maximum date in this range (inclusive) or <code>null</code> if the range has no upper bound.
     */
    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public static DateRange intersection(DateRange a, DateRange b) {

        DateRange i = new DateRange();

		if(a.minDate == null && b.minDate != null) {
			i.minDate = b.minDate;
		} else if(a.minDate != null && b.minDate == null) {
			i.minDate = a.minDate;
		} else if(a.minDate != null && b.minDate != null) {
			if(a.minDate.after(b.minDate)) {
				i.minDate = a.minDate;
			} else {
				i.minDate = b.minDate;
			}
		}

		if(a.maxDate == null && b.maxDate != null) {
			i.maxDate = b.maxDate;
		} else if(a.maxDate != null && b.maxDate == null) {
			i.maxDate = a.maxDate;
		} else if(a.maxDate != null && b.maxDate != null) {
			if(a.maxDate.before(b.maxDate)) {
				i.maxDate = a.maxDate;
			} else {
				i.maxDate = b.maxDate;
			}
		}

        return i;
    }
}
