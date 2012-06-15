package org.activityinfo.shared.command;

import java.util.Date;
import java.util.Set;

import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.google.common.collect.Sets;

/**
 * Serializes/deserializes Filter's fit for use in URLs in the format:
 * 
 * <pre>
 * 	  activity+13+14-database+13-date+2012+20
 * </pre>
 */
public class FilterUrlSerializer {

	private static final String NULL_DATE = "00000000";

	public static String toUrlFragment(Filter filter) {
		StringBuilder url = new StringBuilder();
		for(DimensionType dim : filter.getRestrictedDimensions()) {
			Set<Integer> ids = filter.getRestrictions(dim);
			if(url.length() > 0) {
				url.append("-");
			}
			url.append(dim.name());
			for(Integer id : ids) {
				url.append("+");
				url.append(id);
			}
		}
		if(filter.isDateRestricted()) {
			if(url.length() > 0) {
				url.append("-");
			}
			url.append("date+");
			appendDate(filter.getDateRange().getMinLocalDate(), url);
			url.append('+');
			appendDate(filter.getDateRange().getMaxLocalDate(), url);			
		}
		return url.toString();
	}
	
	public static Filter fromUrlFragment(String fragment) {
		Filter filter = new Filter();
		String[] dimensions = fragment.split("\\-");
		
		for(String dim : dimensions) {
			String[] elements = dim.split("\\+");
			if(elements[0].equals("date")) {
				filter.getDateRange().setMinDate(parseDate(elements[1]));
				filter.getDateRange().setMaxDate(parseDate(elements[2]));
			} else {
				DimensionType dimType = DimensionType.valueOf(elements[0]);
				for(int i=1;i<elements.length;++i) {
					filter.addRestriction(dimType, Integer.parseInt(elements[i]));
				}
			}
		}
		return filter;
	}
	
	public static Filter fromQueryParameter(String value) {
		return fromUrlFragment(value.replace(' ', '+'));
	}
	
	private static LocalDate parseDate(String string) {
		return new LocalDate(
				Integer.parseInt(string.substring(0, 4)),
				Integer.parseInt(string.substring(4, 6)),
				Integer.parseInt(string.substring(6, 8)));
	}

	private static void appendDate(LocalDate date, StringBuilder sb) {
		if(date == null) {
			sb.append(NULL_DATE);
		} else {
			sb.append(date.getYear());
			if(date.getMonthOfYear() < 10) {
				sb.append("0");
			}
			sb.append(date.getMonthOfYear());
			if(date.getDayOfMonth() < 10) {
				sb.append("0");
			}
			sb.append(date.getDayOfMonth());
		}
	}
}
