package org.activityinfo.shared.dto;

import java.util.Date;



import com.extjs.gxt.ui.client.data.BaseModelData;

public class SiteModel extends BaseModelData implements EntityDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7895176170022072389L;

	public static final int STATUS_PLANNED = -2;
	public static final int STATUS_INPROGRESS = -1;
	public static final int STATUS_CANCELLED = 0;
	public static final int STATUS_COMPLETE = 1;

    public static final int REALIZED_SITE = 0;
    public static final int TARGET_SITE = 1;


	public SiteModel()
	{
	}

    public SiteModel(int id) {
        setId(id);
    }

    public SiteModel(SiteModel site) {
        super(site.getProperties());
    }

    public void setId(int value) {
		set("id", value);
	}
	
	public int getId() {
		return (Integer)get("id");
	}
	
	public int getActivityId() {
		return (Integer)get("activityId");
	}
	
	public void setActivityId(int id) {
		set("activityId", id);
	}
	
	public Date getDate1() {
		return get("date1");
	}

	public void setDate1(Date date1) {
		set("date1", date1);
	}

	public Date getDate2() {
		return get("date2");
	}
	
	public void setDate2(Date date2) {
		set("date2", date2);
	}

	public int getStatus() {
		return (Integer)get("status");
	}

	public void setStatus(int status) {
		set("status", status);		
	}	
	
	public String getPartnerName() {
		PartnerModel partner = getPartner();
		if(partner == null) {
			return null;
		} 
		
		return partner.getName();
	}
	
	public PartnerModel getPartner() {
		return get("partner");
	}
	
	public void setPartner(PartnerModel partner) {
		set("partner", partner);
	}
	
	public void setLocationName(String value) {
		set("locationName", value);
	}
	
	public String getLocationName() {
		return get("locationName");
	}

	public String getLocationAxe() {
		return get("locationAxe");
	}

	public void setLocationAxe(String name) {
		set("locationAxe", name);
	}
	
	public void setAdminEntity(int levelId, AdminEntityModel value) {
		set(AdminLevelModel.getPropertyName(levelId), value);	
	}

	public AdminEntityModel getAdminEntity(int levelId) {
		return get(AdminLevelModel.getPropertyName(levelId));
	}
	
	public Object getAdminEntityName(int levelId) {
		AdminEntityModel entity = getAdminEntity(levelId);
		if(entity == null)
			return null;
		
		return entity.getName();
	}


	public void setX(Double x) {
		set("x", x);
	}

	public Double getX() {
		return get("x");
	}

	public Double getY() {
		return get("y");
	}
	
	public void setY(Double y) { 
		set("y", y);
	}

	public boolean hasCoords() {
		return get("x")!=null && get("y")!=null;
	}
	
	public void setAttributeValue(int attributeId, Boolean value) {
		set(AttributeModel.getPropertyName(attributeId), value);
	}
	
	public void setIndicatorValue(int indicatorId, Double value) {
		set(IndicatorModel.getPropertyName(indicatorId), value);	
	}
	
	public Double getIndicatorValue(int indicatorId) {
		return get(IndicatorModel.getPropertyName(indicatorId));
	}
    
    public Double getIndicatorValue(IndicatorModel indicator) {
        return getIndicatorValue(indicator.getId());
    }

	public void setComments(String comments) {
		set("comments", comments);
	}
	
	public String getComments() {
		return get("comments");
	}

	public Boolean getAttributeValue(int id) {
		return get(AttributeModel.getPropertyName(id));
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiteModel siteModel = (SiteModel) o;

        if (getId() != siteModel.getId()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    public boolean hasId() {
        return get("id") != null;
    }

    @Override
    public String getEntityName() {
        return "Site";
    }

    public int getSiteType() {
        Integer siteType = get("siteType");
        return siteType == null ? REALIZED_SITE : siteType;
    }

    public void setSiteType(int type) {
        set("siteType", type);
    }

}
