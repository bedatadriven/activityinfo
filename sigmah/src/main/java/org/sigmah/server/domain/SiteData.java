package org.sigmah.server.domain;

import org.sigmah.server.dao.SiteTableColumn;
import org.sigmah.shared.report.content.SiteGeoData;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A "non-tabular" representation of sites used by 
 * map and narrative generators.
 * 
 * @author Alex Bertram
 *
 */
public class SiteData implements SiteGeoData {

	public Object[] values;
	public Map<Integer, String> adminNames = new HashMap<Integer,String>(0);
    public Map<Integer, AdminEntity> adminEntities = new HashMap<Integer,AdminEntity>(0);
	public Map<Integer, Double> indicatorValues = new HashMap<Integer, Double>(0);
	public Map<Integer, Boolean> attributes = new HashMap<Integer, Boolean>(0);

    public SiteData() {
        this.values = new Object[SiteTableColumn.values().length+1];
    }

	public SiteData(Object[] values) {
        this.values = values;
	}

	public Object getValue(SiteTableColumn column) {
		return values[column.index()];
	}

    public void setValue(SiteTableColumn column, Object value) {
        values[column.index()] = value;
    }
    
	@Override
	public double getLongitude() {
		return (Double) getValue(SiteTableColumn.x);
	}

	@Override
	public double getLatitude() {
		return (Double) getValue(SiteTableColumn.y);
	}
    
    public int getId() {
        return (Integer) getValue(SiteTableColumn.id);
    }

	@Override
	public boolean hasLatLong() {
		return  getValue(SiteTableColumn.y) != null &&
                getValue(SiteTableColumn.x) != null;
	}

    public int getActivityId() {
        return (Integer)values[SiteTableColumn.activity_id.index()];
    }

    public int getDatabaseId() {
        return (Integer)values[SiteTableColumn.database_id.index()];
    }

    public String getLocationName() {
        return (String)values[SiteTableColumn.location_name.index()];
    }

    public String getLocationAxe() {
        return (String)values[SiteTableColumn.location_axe.index()];
    }

    public String getPartnerName() {
        return (String)values[SiteTableColumn.partner_name.index()];
    }

    public Double getIndicatorValue(int id) {
        return indicatorValues.get(id);
    }

    public Date getDate1() {
        return (Date)values[SiteTableColumn.date1.index()];
    }
    
    public Date getDate2() {
        return (Date)values[SiteTableColumn.date2.index()];
    }

    public int getPartnerId() {
        return (Integer)values[SiteTableColumn.partner_id.index()];
    }

    public String getComments() {
        return (String)values[SiteTableColumn.comments.index()];
    }

    public Boolean getAttributeValue(int attribId) {
        return attributes.get(attribId);
    }

    public void setIndicatorValue(int indicatorId, Double value) {
        indicatorValues.put(indicatorId, value);
    }
}
