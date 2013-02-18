/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

import com.bedatadriven.rebar.time.calendar.LocalDate;

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
 *      <td><strong>Meaning</strong></td>
 * </tr>
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
 */
public final class DateRange implements Serializable {


    private LocalDate minDate;

    private LocalDate maxDate;

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
    
    public DateRange(LocalDate minDate, LocalDate maxDate) {
        this.setMinDate(minDate);
        this.setMaxDate(maxDate);
    }
        

    /**
     * Constructs a fully open date range (all dates are included).
     */
    public DateRange() {
        setMinDate((LocalDate)null);
        setMaxDate((LocalDate)null);
    }


    /**
     * Gets the minimum date in this range (inclusive).
     *
     * @return The minimum date in this range (inclusive) or <code>null</code> if the
     * range has no lower bound
     */
    @XmlAttribute(name="min")
    public Date getMinDate() {
        return minDate == null ? null : minDate.atMidnightInMyTimezone();
    }

    public LocalDate getMinLocalDate() {
    	return minDate;
    }
    
    /**
     * Sets the minimum date in this range (inclusive).
     *
     * @param minDate The minimum date in this range (inclusive) or <code>null</code> if the range has
     * now upper bound.
     */
    public void setMinDate(Date minDate) {
        this.minDate = minDate == null ? null : new LocalDate(minDate);
    }
    
    public void setMinDate(LocalDate minDate) {
    	this.minDate = minDate;
    }

    /**
     * Gets the maximum date in this range (inclusive).
     *
     * @return The maximum date in this range (inclusive) or <code>null</code> if the range has no upper bound.
     */
    @XmlAttribute(name="max")
    public Date getMaxDate() {
        return maxDate == null ? null : maxDate.atMidnightInMyTimezone();
    }
    
    public LocalDate getMaxLocalDate() {
    	return maxDate;
    }

    /**
     * Sets the maximum date in this range (inclusive).
     *
     * @param maxDate The maximum date in this range (inclusive) or <code>null</code> if the range has no upper bound.
     */
    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate == null ? null : new LocalDate(maxDate);
    }
  

	public void setMaxDate(LocalDate maxDate) {
		this.maxDate = maxDate;
	}
    

    public static DateRange intersection(DateRange a, DateRange b) {
    	return new DateRange(
    			max(a.getMinDate(), b.getMinDate()),
    			min(a.getMaxDate(), b.getMaxDate()));
    }

    private static Date min(Date a, Date b) {
    	if(a == null && b == null) {
    		return null;
    	} else if(a != null && b != null) {
    		return a.before(b) ? a : b;    		
    	} else if(a != null) {
    		return a;
    	} else {
    		return b;
    	}
    }

    private static Date max(Date a, Date b) {
    	if(a == null && b == null) {
    		return null;
    	} else if(a != null && b != null) {
    		return a.after(b) ? a : b;    		
    	} else if(a != null) {
    		return a;
    	} else {
    		return b;
    	}
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((maxDate == null) ? 0 : maxDate.hashCode());
		result = prime * result + ((minDate == null) ? 0 : minDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DateRange other = (DateRange) obj;
		if (maxDate == null) {
			if (other.maxDate != null) {
				return false;
			}
		} else if (!maxDate.equals(other.maxDate)) {
			return false;
		}
		if (minDate == null) {
			if (other.minDate != null) {
				return false;
			}
		} else if (!minDate.equals(other.minDate)) {
			return false;
		}
		return true;
	}

}
