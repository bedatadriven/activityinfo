package org.activityinfo.shared.report.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;

import org.activityinfo.shared.date.DateRange;
import org.activityinfo.shared.report.model.DimensionType;

public class Filter implements Serializable {

	private Map<DimensionType, Set<Integer>> restrictions = new HashMap<DimensionType, Set<Integer>>();
	
	private Date minDate = null;
	private Date maxDate = null;
	
	
	public Filter() {
	
	}


	public Filter(Filter a, Filter b) {
	
		
		Set<DimensionType> types = new HashSet<DimensionType>();
		types.addAll(a.restrictions.keySet());
		types.addAll(b.restrictions.keySet());
		
		for(DimensionType type : types) {
			this.restrictions.put(type, intersect(
					a.getRestrictionSet(type, false),
					b.getRestrictionSet(type, false)));
			
		}

		if(a.minDate == null && b.minDate != null) {
			minDate = b.minDate;
		} else if(a.minDate != null && b.minDate == null) {
			minDate = a.minDate;
		} else if(a.minDate != null && b.minDate != null) {
			if(a.minDate.after(b.minDate)) {
				minDate = a.minDate;
			} else {
				minDate = b.minDate;
			}
		}
		
		if(a.maxDate == null && b.maxDate != null) {
			maxDate = b.maxDate;
		} else if(a.maxDate != null && b.maxDate == null) {
			maxDate = a.maxDate;
		} else if(a.maxDate != null && b.maxDate != null) {
			if(a.maxDate.before(b.maxDate)) {
				maxDate = a.maxDate;
			} else {
				maxDate = b.maxDate;
			}
		}
	}
	
	private Set<Integer> intersect(Set<Integer> a, Set<Integer> b) {
		if(a.size() == 0)
			return new HashSet<Integer>(b);
		if(b.size() == 0)
			return new HashSet<Integer>(a);
		
		Set<Integer> intersection = new HashSet<Integer>(a);
		intersection.retainAll(b);
		
		return intersection;
	}
	
	public Set<Integer> getRestrictions(DimensionType type) { 
		return getRestrictionSet(type, false);
	}
	
	private Set<Integer> getRestrictionSet(DimensionType type, boolean create) {
		Set<Integer> set = restrictions.get(type);
		
		if(set == null) {
			if(!create) {
				return Collections.emptySet();
			}
			set = new HashSet<Integer>();
			restrictions.put(type, set);
		}
		
		return set;
	}
	
	public void addRestriction(DimensionType type, int categoryId) {
		Set<Integer> set = getRestrictionSet(type, true);
		set.add(categoryId);
	}
	
	public void addRestriction(DimensionType type, Collection<Integer> categoryIds) {
		Set<Integer> set = getRestrictionSet(type, true);
		set.addAll(categoryIds);
	}
	
	public void clearRestrictions(DimensionType type) {
		restrictions.remove(type);
	}
	
	public boolean isRestricted(DimensionType type) {
		return restrictions.containsKey(type);
	}
	
	public boolean isDateRestricted() {
		return minDate!=null || maxDate!=null;
	}

    public Set<DimensionType> getRestrictedDimensions() {
        return new HashSet<DimensionType>(restrictions.keySet());
    }


    private Map<DimensionType, Set<Integer>> getRestrictions() {
        return restrictions;
    }

    private void setRestrictions(Map<DimensionType, Set<Integer>> restrictions) {
        this.restrictions = restrictions;
    }

    public Date getMinDate() {
		return minDate;
	}

	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}


	public Date getMaxDate() {
		return maxDate;
	}


	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

    public void setDateRange(DateRange range) {
        this.minDate = range.date1;
        this.maxDate = range.date2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(DimensionType type : getRestrictedDimensions()) {
            if(sb.length()!=0)
                sb.append(", ");
            sb.append(type.toString()).append("={");
            for(Integer id : getRestrictions(type)) {
                sb.append(' ').append(id);
            }
            sb.append(" }");
        }
        if(minDate!=null || maxDate!=null) {
            if(sb.length()!=0)
                sb.append(", ");
            sb.append("date=[");
            if(minDate!=null)
                sb.append(minDate);
            sb.append(",");
            if(maxDate!=null)
                sb.append(maxDate).append("]");
        }
        return sb.toString();
    }
}
