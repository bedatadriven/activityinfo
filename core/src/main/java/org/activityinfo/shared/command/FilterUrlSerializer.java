package org.activityinfo.shared.command;

import java.util.Set;

import org.activityinfo.shared.report.model.DimensionType;

import com.google.common.collect.Sets;

/**
 * Serializes/deserializes Filter's fit for use in URLs in the format:
 * 
 * <pre>
 * 	  activity+13+14-database+13+ADMIN
 * </pre>
 */
public class FilterUrlSerializer {

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
		return url.toString();
	}
	
	public static Filter fromUrlFragment(String fragment) {
		Filter filter = new Filter();
		String[] dimensions = fragment.split("\\-");
		
		for(String dim : dimensions) {
			String[] elements = dim.split("\\+");
			DimensionType dimType = DimensionType.valueOf(elements[0]);
			Set<Integer> ids = Sets.newHashSet();
			for(int i=1;i<elements.length;++i) {
				ids.add(Integer.parseInt(elements[i]));
			}
			filter.addRestriction(dimType, ids);
		}
		return filter;
	}
}
