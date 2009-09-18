package org.activityinfo.server.report.generator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activityinfo.server.dao.hibernate.PivotDAO;
import org.activityinfo.server.report.util.DateRangeFormat;
import org.activityinfo.shared.report.content.FilterDescription;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.Filter;
import org.activityinfo.shared.report.model.Parameter;
import org.activityinfo.shared.report.model.ParameterizedFilter;
import org.activityinfo.shared.report.model.ReportElement;

import com.google.inject.Inject;

public abstract class BaseGenerator<T extends ReportElement> implements ContentGenerator<T> {
	
	protected final PivotDAO pivotDAO;

	@Inject
	public BaseGenerator(PivotDAO pivotDAO) {
		this.pivotDAO = pivotDAO;
	}

	/**
	 * Resolves a parameterized filter into an actual filter, taking into
	 * account inherited restrictions.
	 * 
	 * @param element
	 * @return
	 */
	protected Filter resolveEffectiveFilter(T element, Filter inheritedFilter, Map<String, Object> parameterValues) {
		Filter filter = ParamFilterHelper.resolve(element.getFilter(), parameterValues);
		if(inheritedFilter != null) {
			filter = new Filter(filter, inheritedFilter);
		}
		return filter;
	}

	protected List<FilterDescription> generateFilterDescriptions(Filter filter, Set<DimensionType> excludeDims) {
	
		List<FilterDescription> list = new ArrayList<FilterDescription>();	
		
		Set<DimensionType> filterDims = filter.getRestrictedDimensions();
		filterDims.removeAll(excludeDims);
		
		for(DimensionType type : filterDims) {
	
			StringBuilder sb = new StringBuilder();
			
			list.add( new FilterDescription(
				type,
				pivotDAO.getFilterLabels(type, filter.getRestrictions(type)) ) );
		}
		
		if(filter.getMinDate() != null || filter.getMaxDate() != null) {
			DateRangeFormat format = new DateRangeFormat();
			
			list.add( new FilterDescription(
				DimensionType.Date,
				format.format(filter.getMinDate(), filter.getMaxDate())));
		}
		
		return list;
		
	}

	
	/**
	 * Resolves a templated string using the supplied parameters. Parameters can be referenced
	 * within the template as ${PARAM_NAME}. Case sensitive.
	 * 
	 * @param template 
	 * @param parameters List of parameter definitions
	 * @param paramValues Parameter values
	 * @return
	 */
	protected String resolveTemplate(String template, List<Parameter> parameters, Map<String, Object> paramValues) {
		
		for(Parameter param : parameters) {
			
			String placeHolder = "${" + param.getName() + "}";
			
			if(template.indexOf(placeHolder) >= 0) {
			
				Object value = paramValues.get(param.getName());
				String formattedValue;
				
				if(param.getType() == Parameter.Type.DATE) {
					
					formattedValue = formatDateParameter(param, (Date) value);
					
				} else {
					
					throw new Error("Not implemented: " + param.getType().toString() );
				}		
				template = template.replace(placeHolder, formattedValue);
			}
		}
		
		return template;
	}
	
	protected String formatDateParameter(Parameter param, Date value) {
		if(param.getDateUnit() == null) {
			DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
		
			return format.format(value);
			
		} else if(param.getDateUnit() == DateUnit.MONTH) {
			SimpleDateFormat format = (SimpleDateFormat)
				DateFormat.getDateInstance();
			format.applyLocalizedPattern("MMM yyyy");
			
			return format.format(value);
		
		} else {
			
			throw new Error("Support for date unit " + param.getDateUnit().toString() + " not yet implemented.");
		}
	}
}
