package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivityModel extends BaseModel implements EntityDTO {

    public final static int REPORT_ONCE = 0;
    public static final int REPORT_MONTHLY = 1;

    private UserDatabaseDTO database;

    private List<IndicatorModel> indicators = new ArrayList<IndicatorModel>(0);
    private List<AttributeGroupModel> attributeGroups = new ArrayList<AttributeGroupModel>(0);

    public ActivityModel() {
        setAssessment(false);
        setReportingFrequency(REPORT_ONCE);
        setAllowEdit(true);
    }

    public ActivityModel(Map<String,Object> properties) {
        super(properties);  
    }

    /**
     * Creates a shallow clone
     *
     * @param model
     */
    public ActivityModel(ActivityModel model) {
        super(model.getProperties());
        this.database = model.getDatabase();
        this.setIndicators(model.getIndicators());
        this.setAttributeGroups(model.getAttributeGroups());
    }

    public ActivityModel(int id, String name) {
        this();
        setId(id);
        setName(name);
    }

    public ActivityModel(UserDatabaseDTO db) {
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

    public List<IndicatorModel> getIndicators()
    {
        return indicators;
    }

    public void setIndicators(List<IndicatorModel> indicators)
    {
        this.indicators = indicators;
    }

    public List<AttributeGroupModel> getAttributeGroups()
    {
        return attributeGroups;
    }

    public void setAttributeGroups(List<AttributeGroupModel> attributes) {
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

    public void setAllowEdit(boolean value) {
        set("allowEdit", value);
    }

    public boolean getAllowEdit()
    {
        return (Boolean)get("allowEdit");
    }


    public void setLocationTypeId(int id) {
        set("locationTypeId", id);

    }

    public int getLocationTypeId() {
        return (Integer)get("locationTypeId");
    }

    public LocationTypeModel getLocationType() {
        return getDatabase().getCountry().getLocationTypeById(getLocationTypeId());
    }


    public AttributeModel getAttributeById(int id) {
        for(AttributeGroupModel group : attributeGroups) {
            AttributeModel attribute = Schema.getById(group.getAttributes(), id);
            if(attribute!=null)
                return attribute;
        }
        return null;
    }

    public IndicatorModel getIndicatorById(int indicatorId) {
        for(IndicatorModel indicator : indicators) {
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

        for(IndicatorModel indicator : indicators) {
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
    public List<AdminLevelModel> getAdminLevels() {
        if(getLocationType().isAdminLevel()) {

            // if this activity is bound to an administative
            // level, then we need only as far down as this goes

            return getDatabase().getCountry().getAncestors(
                    getLocationType().getBoundAdminLevelId());
        } else {

            // all admin levels

            return getDatabase().getCountry().getAdminLevels();
        }
    }


    public AttributeGroupModel getAttributeGroupById(int id) {
        for(AttributeGroupModel group : attributeGroups) {
            if(group.getId()==id)
                return group;

        }

        return null;
    }
}
