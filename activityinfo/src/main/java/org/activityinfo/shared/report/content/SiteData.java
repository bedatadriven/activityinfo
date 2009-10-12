package org.activityinfo.shared.report.content;

import org.activityinfo.shared.domain.SiteColumn;

import java.util.*;

/**
 * A "non-tabular" representation of sites used by 
 * map and narrative generators.
 * 
 * @author Alex Bertram
 *
 */
public class SiteData implements SiteGeoData {

	public Object[] values;
	public Map<Integer, String> admin = new HashMap<Integer,String>(0);
	public Map<Integer, Double> indicatorValues = new HashMap<Integer, Double>(0);
	public Set<Integer> attributes = new HashSet<Integer>(0);                                           
	
    public SiteData() {
        this.values = new Object[SiteColumn.values().length+1];
    }

	public SiteData(Object[] values) {
        this.values = values;
	}

	public Object getValue(SiteColumn column) {
		return values[column.index()];
	}

    public void setValue(SiteColumn column, Object value) {
        values[column.index()] = value;
    }
    
	@Override
	public double getLongitude() {
		return (Double) getValue(SiteColumn.x);
	}

	@Override
	public double getLatitude() {
		return (Double) getValue(SiteColumn.y);
	}
    
    public int getId() {
        return (Integer) getValue(SiteColumn.id);
    }

	@Override
	public boolean hasLatLong() {
		return  getValue(SiteColumn.y) != null &&
                getValue(SiteColumn.x) != null;
	}

    public int getActivityId() {
        return (Integer)values[SiteColumn.activity_id.index()];
    }

    public int getDatabaseId() {
        return (Integer)values[SiteColumn.database_id.index()];
    }

    public String getLocationName() {
        return (String)values[SiteColumn.location_name.index()];
    }

    public String getLocationAxe() {
        return (String)values[SiteColumn.location_axe.index()];
    }

    public String getPartnerName() {
        return (String)values[SiteColumn.partner_name.index()];
    }

    public Double getIndicatorValue(int id) {
        return indicatorValues.get(id);
    }

    public Date getDate1() {
        return (Date)values[SiteColumn.date1.index()];
    }
    
    public Date getDate2() {
        return (Date)values[SiteColumn.date2.index()];
    }

    public int getPartnerId() {
        return (Integer)values[SiteColumn.partner_id.index()];
    }
}
