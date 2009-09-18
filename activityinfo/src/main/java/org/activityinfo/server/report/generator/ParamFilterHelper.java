package org.activityinfo.server.report.generator;

import org.activityinfo.server.util.DateUtilCalendarImpl;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.Filter;
import org.activityinfo.shared.report.model.ParameterizedFilter;
import org.activityinfo.shared.report.model.ParameterizedValue;

import java.util.*;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ParamFilterHelper {


	public static Filter resolve(ParameterizedFilter pfilter, Map<String,Object> parameters) {

        DateUtilCalendarImpl dateUtil = new DateUtilCalendarImpl(); ;

		Filter filter = new Filter();
		for(DimensionType type : pfilter.getRestrictedDimensions()) {
			filter.addRestriction(type, resolveSet(pfilter.getRestrictions(type), parameters));
		}

		if(pfilter.getMinDate()!=null) {

			Date date = pfilter.getMinDate().resolve(parameters);

			if(pfilter.getDateUnit() == null) {
				filter.setMinDate(date);
			} else {
				filter.setMinDate(dateUtil.floor(date, pfilter.getDateUnit()));

				if(pfilter.getCount() != null) {
					filter.setMaxDate(dateUtil.add(date, pfilter.getDateUnit(),
                            +pfilter.getCount().resolve(parameters)));
				}
			}

		}

		if(pfilter.getMaxDate()!=null) {

			Date date = pfilter.getMaxDate().resolve(parameters);

			if(pfilter.getDateUnit() == null) {
				filter.setMaxDate(date);
			} else {
				filter.setMaxDate(dateUtil.ceil(date, pfilter.getDateUnit()));

				if(pfilter.getCount() != null) {
					filter.setMinDate(dateUtil.add(date, pfilter.getDateUnit(),
                            -pfilter.getCount().resolve(parameters)));
				}
			}
		}

		return filter;
	}


	private static Set<Integer> resolveSet(List<ParameterizedValue<Integer>> values, Map<String,Object> parameters) {
		Set<Integer> set = new HashSet<Integer>(values.size());

		for(ParameterizedValue<Integer> value : values) {
			if(value.getLiteral() != null) {
				set.add(value.getLiteral());
			} else {
				set.add((Integer)parameters.get(value.getParameterName()));
			}
		}
		return set;
	}
}
