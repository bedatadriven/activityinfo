/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

import org.sigmah.shared.report.model.typeadapter.FilterAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.*;

/**
 * Defines a filter of activity data as a date range and a set of restrictions on
 * <code>Dimensions</code>.
 *
 */
@XmlJavaTypeAdapter(FilterAdapter.class)
public class Filter implements Serializable {

    // TODO: should be restrictions on DIMENSIONS and not DimensionTypes!!

	private Map<DimensionType, Set<Integer>> restrictions = new HashMap<DimensionType, Set<Integer>>();

 	private DateRange dateRange = new DateRange();


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
        this.dateRange = filter.dateRange;
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
        this.dateRange = DateRange.intersection(a.getDateRange(), b.getDateRange());

	}
	
	private Set<Integer> intersect(Set<Integer> a, Set<Integer> b) {
		if(a.size() == 0) {
            return new HashSet<Integer>(b);
        }
		if(b.size() == 0) {
            return new HashSet<Integer>(a);
        }
		
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
		return dateRange.getMinDate()!=null || dateRange.getMaxDate()!=null;
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

    @XmlTransient
    public Date getMinDate() {
		return getDateRange().getMinDate();
	}

	public void setMinDate(Date minDate) {
		getDateRange().setMinDate(minDate);
	}

    @XmlTransient
	public Date getMaxDate() {
		return getDateRange().getMaxDate();
	}

	public void setMaxDate(Date maxDate) {
		getDateRange().setMaxDate(maxDate);
	}

    public void setDateRange(DateRange range) {
        this.dateRange = range;
    }

    @XmlElement
    public DateRange getDateRange() {
        if(dateRange == null) {
            dateRange = new DateRange();
        }
        return dateRange;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(DimensionType type : getRestrictedDimensions()) {
            if(sb.length()!=0) {
                sb.append(", ");
            }
            sb.append(type.toString()).append("={");
            for(Integer id : getRestrictions(type)) {
                sb.append(' ').append(id);
            }
            sb.append(" }");
        }
        if(dateRange.getMinDate()!=null || dateRange.getMaxDate()!=null) {
            if(sb.length()!=0) {
                sb.append(", ");
            }
            sb.append("date=[");
            if(dateRange.getMinDate()!=null) {
                sb.append(dateRange.getMinDate());
            }
            sb.append(",");
            if(dateRange.getMaxDate()!=null) {
                sb.append(dateRange.getMaxDate()).append("]");
            }
        }
        return sb.toString();
    }


}
