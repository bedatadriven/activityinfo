package org.activityinfo.server.report.generator;

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

import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.ReportElement;

public class GeneratorUtils {

	/**
	 * Resolves an element's filter into a the effective filter, taking into
	 * account inherited restrictions and the overall <code>DateRange</code> of the
	 * report.
	 * <p/>
	 * Interaction between the report's date range <code>DateRange</code> and the
	 * element's filter is specified in {@link org.activityinfo.shared.report.model.ReportElement#getFilter()}
	 *
	 * @param element         The report element for which to resolve the filter
	 * @param inheritedFilter The <code>Filter</code> that is inherited from the enclosing <code>Report</code> or other container
	 * @param dateRange       The overall <code>DateRange</code> of the report. This may be <code>null</code>, for example if generation is not
	 *                        occuring in the context of an individual element.
	 * @return the effective <code>Filter</code>
	 */
	public static Filter resolveEffectiveFilter(ReportElement element, Filter inheritedFilter, DateRange dateRange) {
	
	    Filter filter;
	    if (inheritedFilter != null) {
	        filter = new Filter(element.getFilter(), inheritedFilter);
	    } else {
	        filter = new Filter(element.getFilter());
	    }
	    return resolveElementFilter(element, dateRange);
	}

	public static Filter resolveElementFilter(ReportElement element, DateRange dateRange) {
	
	    Filter filter = new Filter(element.getFilter());
	
	    if (dateRange != null) {
	        if (filter.getMinDate() == null) {
	            filter.setMinDate(dateRange.getMinDate());
	        }
	        if (filter.getMaxDate() == null) {
	            filter.setMaxDate(dateRange.getMaxDate());
	        }
	    }
	    return filter;
	}

}
