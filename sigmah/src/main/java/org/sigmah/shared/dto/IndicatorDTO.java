/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package	org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModel;


/**
 * One-to-one DTO for the {@link org.sigmah.server.domain.Indicator} domain object.
 *
 * @author Alex Bertram
 */
public final class IndicatorDTO extends BaseModel implements EntityDTO {
	public final static int AGGREGATE_SUM = 0;
	public final static int AGGREGATE_AVG = 1;
	public final static int AGGREGATE_SITE_COUNT = 2;

    public static final String PROPERTY_PREFIX = "I";
	
	public IndicatorDTO() {
	}
	
	public IndicatorDTO(String name, String units ) {
		set("name", name);
		set("units", units);		
	}

    public IndicatorDTO(IndicatorDTO model) {
        super(model.getProperties());
    }

    public int getId() {
        return (Integer)get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    public void setName(String value) {
		set("name", value);
	}
	
	public String getName() {
		return get("name");		
	}
	
	public void setUnits(String value) {
		set("units",value);
	}
	
	public String getUnits() {
		return get("units");	
	}

	public String getListHeader() {
		return get("listHeader");
	}	
	
	public void setDescription(String value) {
		set("description", value);
	}
	
	public String getDescription() {
		return get("description");
	}

	public void setCollectIntervention(boolean value) {
		set("collectIntervention", value);
	}
	
	public boolean getCollectIntervention() {
		return (Boolean)get("collectIntervention");
	}
	
	public void setCollectMonitoring(boolean value) {
		set("collectMonitoring",value);
	}
	
	public boolean getCollectMonitoring() { 
		return (Boolean)get("collectMonitoring");
	}
	
	public void setListHeader(String value) {
		set("listHeader", value);
	}

	public void setAggregation(int value) {
		set("aggregation", value);
	}
	
	public int getAggregation() {
		return (Integer)get("aggregation");
	}

	public String getCategory() {
		return get("category");
	}

	public void setCategory(String value) {
		set("category", value);
		
	}

    /**
     *
     * @return the name of the property in which values for this indicator are stored, for
     * example in the {@link org.sigmah.shared.dto.SiteDTO} object.
     *
     */
	public String getPropertyName() { 
		return getPropertyName(this.getId());
	}

    /**
     * Returns the name of the property in which values for Indicators of this id are stored, for
     * example in the {@link org.sigmah.shared.dto.SiteDTO} object.
     *
     * For example, an indicator with the id of 3 will be stored as I3 => 1432.32 in a
     * SiteDTO.
     *
     * @param id
     * @return the property name for
     */
	public static String getPropertyName(int id) {
		return PROPERTY_PREFIX + id;
	}

	public static int indicatorIdForPropertyName(String property) {
		return Integer.parseInt(property.substring(PROPERTY_PREFIX.length()));
	}

    public String getEntityName() {
        return "Indicator";
    }
}
