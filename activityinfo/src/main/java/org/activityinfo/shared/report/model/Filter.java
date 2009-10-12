package org.activityinfo.shared.report.model;

import org.activityinfo.shared.date.DateRange;

import java.io.Serializable;
import java.util.*;

/**
 * Defines a filter of activity data as a date range and a set of restrictions on
 * <code>Dimensions</code>.
 *
 */
public class Filter implements Serializable {

    // TODO: should be restrictions on DIMENSIONS and not DimensionTypes!!

	private Map<DimensionType, Set<Integer>> restrictions = new HashMap<DimensionType, Set<Integer>>();
	
	private Date minDate = null;
	private Date maxDate = null;


    /**
     * Constructs a <code>Filter</code> with no restrictions. All data visible to the user
     * will be included.
     */
	public Filter() {

	}

    /**
     * Constructs a copy of the given <code>filter</code>
     *
     * @param filter The filter which to copy.
     */
    public Filter(Filter filter) {

        for(Map.Entry<DimensionType, Set<Integer>> entry : filter.restrictions.entrySet()) {
            this.restrictions.put(entry.getKey(), new HashSet<Integer>(entry.getValue()));
        }
        this.minDate = filter.minDate;
        this.maxDate = filter.maxDate;
    }


    /**
     * Constructs a <code>Filter</code> as the intersection between two <code>Filter</code>s.
     *
     * @param a The first filter
     * @param b The second filter
     */
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
        this.minDate = range.getMinDate();
        this.maxDate = range.getMaxDate();
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
