/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

import java.util.Date;

/**
 * Projection DTO for the {@link org.sigmah.server.domain.Site} domain object, including
 * its {@link org.sigmah.server.domain.Location Location}, and
 * {@link org.sigmah.server.domain.ReportingPeriod ReportingPeriod} totals
 *
 * @author Alex Bertram
 */
public final class SiteDTO extends BaseModelData implements EntityDTO {

    public SiteDTO() {
	}

    public SiteDTO(int id) {
        setId(id);
    }

    public SiteDTO(SiteDTO site) {
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
		PartnerDTO partner = getPartner();
		if(partner == null) {
			return null;
		} 
		
		return partner.getName();
	}
	
	public PartnerDTO getPartner() {
		return get("partner");
	}
	
	public void setPartner(PartnerDTO partner) {
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
	
	public void setAdminEntity(int levelId, AdminEntityDTO value) {
		set(AdminLevelDTO.getPropertyName(levelId), value);
	}

	public AdminEntityDTO getAdminEntity(int levelId) {
		return get(AdminLevelDTO.getPropertyName(levelId));
	}
	
	public Object getAdminEntityName(int levelId) {
		AdminEntityDTO entity = getAdminEntity(levelId);
		if(entity == null) {
            return null;
        }
		
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
		set(AttributeDTO.getPropertyName(attributeId), value);
	}
	
	public void setIndicatorValue(int indicatorId, Double value) {
		set(IndicatorDTO.getPropertyName(indicatorId), value);
	}
	
	public Double getIndicatorValue(int indicatorId) {
		return get(IndicatorDTO.getPropertyName(indicatorId));
	}
    
    public Double getIndicatorValue(IndicatorDTO indicator) {
        return getIndicatorValue(indicator.getId());
    }

	public void setComments(String comments) {
		set("comments", comments);
	}
	
	public String getComments() {
		return get("comments");
	}

	public Boolean getAttributeValue(int id) {
		return get(AttributeDTO.getPropertyName(id));
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SiteDTO siteModel = (SiteDTO) o;

        if (getId() != siteModel.getId()) {
            return false;
        }

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
}
