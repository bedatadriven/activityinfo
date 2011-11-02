package org.sigmah.server.report.generator;

import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.ReportElement;

public class GeneratorUtils {

	/**
	 * Resolves an element's filter into a the effective filter, taking into
	 * account inherited restrictions and the overall <code>DateRange</code> of the
	 * report.
	 * <p/>
	 * Interaction between the report's date range <code>DateRange</code> and the
	 * element's filter is specified in {@link org.sigmah.shared.report.model.ReportElement#getFilter()}
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
