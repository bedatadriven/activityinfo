/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package	org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;


/**
 * One-to-one DTO for the {@link org.sigmah.shared.domain.Indicator} domain object.
 *
 * @author Alex Bertram
 */
public final class IndicatorDTO extends BaseModelData implements EntityDTO {
	public final static int AGGREGATE_SUM = 0;
	public final static int AGGREGATE_AVG = 1;
	public final static int AGGREGATE_SITE_COUNT = 2;

    public static final String PROPERTY_PREFIX = "I";
	
	public IndicatorDTO() {
        super();
	}

    /**
     *
     * @param name  the name of the Indicator
     * @param units  string describing this Indicator's units
     */
	public IndicatorDTO(String name, String units) {
        super();
		set("name", name);
		set("units", units);		
	}

    /**
     * Constructs a copy of the given IndicatorDTO
     */
    public IndicatorDTO(IndicatorDTO dto) {
        super(dto.getProperties());
    }

    /**
     *
     * @return the id of the Indicator
     */
    public int getId() {
        return (Integer)get("id");
    }

    /**
     * Sets the Indicator's id
     */
    public void setId(int id) {
        set("id", id);
    }

    /**
     * Sets the Indicator's name
     */
    public void setName(String name) {
		set("name", name);
	}

    /**
     * @return the Indicator's name
     */
	public String getName() {
		return get("name");		
	}

    /**
     * Sets the Indicator's units, for example, "household" or  "%"
     */
	public void setUnits(String units) {
		set("units",units);
	}

    /**
     * @return the Indicator's units
     */
	public String getUnits() {
		return get("units");	
	}

    /**
     * @return the short list header used when displaying this Indicator in a grid
     */
	public String getListHeader() {
		return get("listHeader");
	}

    /**
     * Sets the short list header that is used when this Indicator's values are displayed
     * in a grid.
     */
	public void setListHeader(String value) {
		set("listHeader", value);
	}

    /**
     * Full description of this Indicator, used to aid users entering data.
     */
	public void setDescription(String description) {
		set("description", description);
	}

    /**
     * @return this Indicator's description, principally used to aid users entering data
     */
	public String getDescription() {
		return get("description");
	}

    /**
     * Sets whether this indicator is collected during the intervention.
     * (Some indicators are only collected during the monitoring phase)
     */
	public void setCollectIntervention(boolean collected) {
		set("collectIntervention", collected);
	}

    /**
     * @return true if this indicator is collected during the intervention.
     * (Some indicators are only collected during the monitoring phase)
     */
	public boolean isCollectIntervention() {
		return (Boolean)get("collectIntervention");
	}

    /**
     * Sets whether this indicator is collected during the monitoring phase/
     */
	public void setCollectMonitoring(boolean collected) {
		set("collectMonitoring",collected);
	}

    /**
     * @return true if this indicator is collected during the monitoring phase
     */
	public boolean isCollectMonitoring() {
		return (Boolean)get("collectMonitoring");
	}

    /**
     * Sets the aggregation method for this indicator
     */
	public void setAggregation(int aggregation) {
		set("aggregation", aggregation);
	}

    /**
     * @return the aggregation method for this indicator
     */
	public int getAggregation() {
		return (Integer)get("aggregation");
	}

    /**
     * @return this Indicator's category
     */
	public String getCategory() {
		return get("category");
	}

    /**
     * Sets this Indicator's category
     */
	public void setCategory(String category) {
		set("category", category);
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

    /**
     * Parses an Indicator property name, for example "I432" or "I565" for the referenced
     * indicator Id.
     *
     * @return the id of referenced Indicator
     */
	public static int indicatorIdForPropertyName(String propertyName) {
		return Integer.parseInt(propertyName.substring(PROPERTY_PREFIX.length()));
	}

    public String getEntityName() {
        return "Indicator";
    }
}
