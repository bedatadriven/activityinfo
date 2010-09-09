/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;

import org.sigmah.shared.dao.SiteTableColumn;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.map.HasLatLng;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A non-tabular representation of sites used by 
 * map and narrative generators.
 * 
 * @author Alex Bertram
 *
 */
public class SiteData implements HasLatLng {

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

    /**
     * @param column
     * @return the value of the given column
     */
	public Object getValue(SiteTableColumn column) {
		return values[column.index()];
	}

    /**
     * Sets the value of the given column for this Site
     */
    public void setValue(SiteTableColumn column, Object value) {
        values[column.index()] = value;
    }

    /**
     * @return the longitude of this Site
     */
	@Override
	public double getLongitude() {
		return (Double) getValue(SiteTableColumn.x);
	}

    /**
     *
     * @return the latitude of this Site
     */
	@Override
	public double getLatitude() {
		return (Double) getValue(SiteTableColumn.y);
	}

    /**
     *
     * @return this Site's ID
     */
    public int getId() {
        return (Integer) getValue(SiteTableColumn.id);
    }

    /**
     *
     * @return true if this Site has non-null lat and long
     */
	@Override
	public boolean hasLatLong() {
		return  getValue(SiteTableColumn.y) != null &&
                getValue(SiteTableColumn.x) != null;
	}

    /**
     *
     * @return the id of the Activity to which this Site belongs
     */
    public int getActivityId() {
        return (Integer)values[SiteTableColumn.activity_id.index()];
    }

    /**
     *
     * @return the id of the database to which this Site belongs
     */
    public int getDatabaseId() {
        return (Integer)values[SiteTableColumn.database_id.index()];
    }

    /**
     *
     * @return the name of location with which this Site is associated
     */
    public String getLocationName() {
        return (String)values[SiteTableColumn.location_name.index()];
    }

    /**
     *
     * @return the axe routier of the location with which this Site is associated
     */
    public String getLocationAxe() {
        return (String)values[SiteTableColumn.location_axe.index()];
    }

    /**
     *
     * @return  the name of the Partner who owns this Site
     */
    public String getPartnerName() {
        return (String)values[SiteTableColumn.partner_name.index()];
    }

    /**
     * @return the total value of the indicator with the given Id
     */
    public Double getIndicatorValue(int indicatorId) {
        return indicatorValues.get(indicatorId);
    }


    /**
     *
     * @return  the date on which work at this Site began
     */
    public Date getDate1() {
        return (Date)values[SiteTableColumn.date1.index()];
    }

    /**
     *
     * @return  the date on which work at this Site ended
     */
    public Date getDate2() {
        return (Date)values[SiteTableColumn.date2.index()];
    }

    /**
     *
     * @return  the id of the Partner who owns this Site
     */
    public int getPartnerId() {
        return (Integer)values[SiteTableColumn.partner_id.index()];
    }

    /**
     *
     * @return  the plain-text comments associated with this Site
     */
    public String getComments() {
        return (String)values[SiteTableColumn.comments.index()];
    }

    /**
     * @return the value of the Attribute with the given Id for this Site
     */
    public Boolean getAttributeValue(int attributeId) {
        return attributes.get(attributeId);
    }

    /**
     * Sets the total value for the indicator at this Site
     */
    public void setIndicatorValue(int indicatorId, Double value) {
        indicatorValues.put(indicatorId, value);
    }
}
