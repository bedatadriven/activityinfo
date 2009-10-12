package org.activityinfo.shared.report.model;

import com.extjs.gxt.ui.client.data.BaseModel;

import java.util.*;

/**
 * Models a parameterized filter
 * 
 * @author Alex Bertram
 *
 */
public class ParameterizedFilter extends BaseModel {

	
	private Map<DimensionType, List<ParameterizedValue<Integer>>> restrictions = new HashMap<DimensionType, List<ParameterizedValue<Integer>>>();
		
	private ParameterizedValue<Date> minDate = null;
	private ParameterizedValue<Date> maxDate = null;
	private DateUnit dateUnit = null;
	private ParameterizedValue<Integer> count = null;
	
	public ParameterizedFilter() {
		
		
	}

	private List<ParameterizedValue<Integer>> getRestrictionList(DimensionType type, boolean create) {
		List<ParameterizedValue<Integer>> list = restrictions.get(type);
		if(list == null) {
			if(!create){ 
				return Collections.emptyList();
			}
			list = new ArrayList<ParameterizedValue<Integer>>();
			restrictions.put(type, list);
		}
		return list;
	}
	
	public void addRestriction(DimensionType type, int... ids) {
		List<ParameterizedValue<Integer>> list = getRestrictionList(type, true);
		for(int id : ids) {
			list.add(ParameterizedValue.literal(id));
		}
	}
	
	public void addRestriction(DimensionType type, ParameterizedValue<Integer> value) {
		List<ParameterizedValue<Integer>> list = getRestrictionList(type, true);
		list.add(value);
	}
	
	public List<ParameterizedValue<Integer>> getRestrictions(DimensionType type) {
		return getRestrictionList(type, false);
	}
	
	public Set<DimensionType> getRestrictedDimensions() {
		return restrictions.keySet();
	}
	
	public ParameterizedValue<Date> getMinDate() {
		return minDate;
	}

	public void setMinDate(ParameterizedValue<Date> minDate) {
		this.minDate = minDate;
	}
	
	public void setMinDate(Date minDate) {
        if(minDate == null)
            this.minDate = null;
        else
		    setMinDate(ParameterizedValue.literal(minDate));
	}


    public ParameterizedValue<Date> getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(ParameterizedValue<Date> maxDate) {
		this.maxDate = maxDate;
	}
	
	public void setMaxDateParameter(String name) {
		this.maxDate = ParameterizedValue.<Date>forParam(name);
	}
	
	public void setMinDateParameter(String name) {
		this.minDate = ParameterizedValue.<Date>forParam(name);
	}
	
	public void setMaxDate(Date maxDate) {
		setMaxDate(ParameterizedValue.literal(maxDate));
	}

	
	public DateUnit getDateUnit() {
		return dateUnit;
	}

	public void setDateUnit(DateUnit dateUnit) {
		this.dateUnit = dateUnit;
	}

	public ParameterizedValue<Integer> getCount() {
		return count;
	}

	public void setCount(ParameterizedValue<Integer> count) {
		this.count = count;
	}
	
	public void setCount(int count) {
		this.count = ParameterizedValue.literal(count);
	}


}
