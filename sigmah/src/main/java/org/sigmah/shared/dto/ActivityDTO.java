/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * One-to-one DTO for the {@link org.sigmah.server.domain.Activity} domain object.
 *
 * @author Alex Bertram
 */
public final class ActivityDTO extends BaseModelData implements EntityDTO {

    public final static int REPORT_ONCE = 0;
    public static final int REPORT_MONTHLY = 1;

    private UserDatabaseDTO database;

    private List<IndicatorDTO> indicators = new ArrayList<IndicatorDTO>(0);
    private List<AttributeGroupDTO> attributeGroups = new ArrayList<AttributeGroupDTO>(0);

    public ActivityDTO() {
        setAssessment(false);
        setReportingFrequency(REPORT_ONCE);
    }

    public ActivityDTO(Map<String,Object> properties) {
        super(properties);  
    }

    /**
     * Creates a shallow clone
     *
     * @param model
     */
    public ActivityDTO(ActivityDTO model) {
        super(model.getProperties());
        this.database = model.getDatabase();
        this.setIndicators(model.getIndicators());
        this.setAttributeGroups(model.getAttributeGroups());
    }

    public ActivityDTO(int id, String name) {
        this();
        setId(id);
        setName(name);
    }

    public ActivityDTO(UserDatabaseDTO db) {
        setDatabase(db);
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

    public String getName(){
        return get("name");
    }

    public UserDatabaseDTO getDatabase() {
        return database;
    }

    public void setDatabase(UserDatabaseDTO database) {
        this.database = database;
    }

    public List<IndicatorDTO> getIndicators()
    {
        return indicators;
    }

    public void setIndicators(List<IndicatorDTO> indicators)
    {
        this.indicators = indicators;
    }

    public List<AttributeGroupDTO> getAttributeGroups()
    {
        return attributeGroups;
    }

    public void setAttributeGroups(List<AttributeGroupDTO> attributes) {
        this.attributeGroups = attributes;
    }

    public void setAssessment(boolean value) {
        set("assessment", value);
    }

    public boolean isAssessment() {
        return (Boolean)get("assessment");
    }

    public void setReportingFrequency(int value) {
        set("reportingFrequency", value);
    }

    public int getReportingFrequency()
    {
        return (Integer)get("reportingFrequency");
    }


    public void setLocationTypeId(int id) {
        set("locationTypeId", id);

    }

    public int getLocationTypeId() {
        return (Integer)get("locationTypeId");
    }

    public LocationTypeDTO getLocationType() {
        return getDatabase().getCountry().getLocationTypeById(getLocationTypeId());
    }


    public AttributeDTO getAttributeById(int id) {
        for(AttributeGroupDTO group : attributeGroups) {
            AttributeDTO attribute = SchemaDTO.getById(group.getAttributes(), id);
            if(attribute!=null) {
                return attribute;
            }
        }
        return null;
    }

    public IndicatorDTO getIndicatorById(int indicatorId) {
        for(IndicatorDTO indicator : indicators) {
            if(indicator.getId() == indicatorId) {
                return indicator;
            }
        }
        return null;
    }

    public String getCategory() {
        return get("category");
    }

    public void setCategory(String category) {
        set("category", category);
    }

    public List<IndicatorGroup> groupIndicators() {
        List<IndicatorGroup> groups = new ArrayList<IndicatorGroup>();
        Map<String, IndicatorGroup> map = new HashMap<String, IndicatorGroup>();

        for(IndicatorDTO indicator : indicators) {
            String category = indicator.getCategory();
            IndicatorGroup group = map.get(category);
            if(group == null) {
                group = new IndicatorGroup(category);
                map.put(category, group);
                groups.add(group);
            }
            group.addIndicator(indicator);
        }
        return groups;
    }

    public String getMapIcon() {
        return get("mapIcon");
    }

    public void setMapIcon(String mapIcon) {
        set("mapIcon", mapIcon);
    }

    public String getEntityName() {
        return "Activity";
    }

    /**
     *
     * @return The list of admin levels that are relevant for the
     * level of aggregation of this activity
     */
    public List<AdminLevelDTO> getAdminLevels() {
        if(getLocationType().isAdminLevel()) {

            // if this activity is bound to an administative
            // level, then we need only as far down as this goes

            return getDatabase().getCountry().getAdminLevelAncestors(
                    getLocationType().getBoundAdminLevelId());
        } else {

            // all admin levels

            return getDatabase().getCountry().getAdminLevels();
        }
    }


    public AttributeGroupDTO getAttributeGroupById(int id) {
        for(AttributeGroupDTO group : attributeGroups) {
            if(group.getId()==id) {
                return group;
            }

        }

        return null;
    }
}
