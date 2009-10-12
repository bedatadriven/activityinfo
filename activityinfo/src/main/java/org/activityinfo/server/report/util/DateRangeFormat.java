package org.activityinfo.server.report.util;

import org.activityinfo.shared.date.DateRange;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateRangeFormat {
	
	
	private DateFormat medium;
	private DateFormat month;
	private DateFormat monthYear;
	private String rangePattern;
	private String afterPattern;
	private String beforePattern;
	
	public DateRangeFormat(Locale locale) {
		
		medium = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		month = getMonthFormat(locale);
		monthYear = getMonthYearFormat(locale);
		rangePattern = "%s - %s";
		beforePattern = "Activites termines jusqu'a %s";    //TODO i18n
		afterPattern = "Activites termines dans / apres %s";
	}

    public String format(DateRange range) {
        return format(range.getMinDate(), range.getMaxDate());
    }

	public String format(Date min, Date max) {
		StringBuilder text = new StringBuilder();
		
		if(min != null && max == null) {
			
			return String.format(afterPattern, medium.format(min));
			
		} else if(max != null && min == null) {
			
			return String.format(beforePattern, medium.format(max));

		} else if(min != null && max != null) {
			
			Calendar d1 = Calendar.getInstance();
			d1.setTime(min);
			
			Calendar d2 = Calendar.getInstance();
			d2.setTime(max);

			return format(d1, d2);
			
		} else {
			return null;
		}
		
	}
	
	protected String format(Calendar min, Calendar max) {


		
		if(isMin(min, Calendar.MONTH) && isMin(min, Calendar.DATE) &&
		   isMax(max, Calendar.MONTH) && isMax(max, Calendar.DATE) ) {

			/* Case 1 - Range of years */
			
			if(min.get(Calendar.YEAR) == max.get(Calendar.YEAR)) {
				
				/* Case 1a - Single Year */
				
				return Integer.toString(min.get(Calendar.YEAR));
				
			} else {
				
				/* Case 1b - Multiple Years */
				
				return String.format(rangePattern,
						Integer.toString(min.get(Calendar.YEAR)),
						Integer.toString(max.get(Calendar.YEAR)));
			}			
		} else if(isMin(min, Calendar.DATE) && isMax(max, Calendar.DATE)) {
			
			/* Case 2 - Range of months */
			
			if(min.get(Calendar.MONTH) == max.get(Calendar.MONTH) &&
			   min.get(Calendar.YEAR) == max.get(Calendar.YEAR) ) {
				
				/* Case 2a Single month */
				
				return monthYear.format(min.getTime());
			
			} else if(min.get(Calendar.YEAR) == max.get(Calendar.YEAR)){
				
				/* Case 2b Multiple months in same year */
				
				return String.format(rangePattern, 
						month.format(min.getTime()),
						monthYear.format(max.getTime()));
				
			} else {
				
				/* Case 3b multiple months over multiple years */
				
				
				return String.format(rangePattern, 
						monthYear.format(min.getTime()),
						monthYear.format(max.getTime()));
			}
			
		} else {

			return String.format(rangePattern, 
					medium.format(min.getTime()),
					medium.format(max.getTime()));
		}
		
	}
	
	protected boolean isMax(Calendar c, int field) {
		return c.get(field) == c.getMaximum(field);
	}
	
	protected boolean isMin(Calendar c, int field) {
		return c.get(field) == c.getMinimum(field);
	}
	
	protected DateFormat getMonthYearFormat(Locale locale) {
		SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT,locale);
		format.applyPattern("MMM yyyy");
		
		return format;
	}
	
	protected DateFormat getMonthFormat(Locale locale) {
		SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, locale);
		format.applyPattern("MMM");
		
		return format;
	}
}
