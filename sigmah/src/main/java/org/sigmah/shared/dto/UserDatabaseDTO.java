package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModel;

import java.util.ArrayList;
import java.util.List;


/**
 * One-to-one DTO of the {@link org.sigmah.server.domain.UserDatabase} domain object.
 *
 * @author Alex Bertram
 */
public final class UserDatabaseDTO extends BaseModel implements EntityDTO {

    private CountryDTO country;
	private List<PartnerDTO> partners = new ArrayList<PartnerDTO>(0);
	private List<ActivityDTO> activities = new ArrayList<ActivityDTO>(0);

    public UserDatabaseDTO() {
	}

    public UserDatabaseDTO(int id, String name) {
        setId(id);
        setName(name);
    }

    public int getId() {
        return (Integer)get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    public String getName() {
		return get("name");
	}
	
	public void setName(String value) {
		set("name", value);
	}
	
	public void setOwnerName(String value) {
		set("ownerName", value);
	}
	
	public String getOwnerName() {
		return get("ownerName");
	}
	
	public void setOwnerEmail(String value) { 
		set("ownerEmail", value);
	}
	
	public String getOwnerEmail() {
		return get("ownerEmail");
	}

	public void setFullName(String fullName) {
		set("fullName", fullName);
	}
	
	public String getFullName() {
		return get("fullName");
	}

	public List<ActivityDTO> getActivities() {
		return activities;
	}
	
	public void setActivities(List<ActivityDTO> activities) {
		this.activities = activities;
	}
	
	public CountryDTO getCountry() 	{
		return country;
	}

	public void setCountry(CountryDTO value) {
		country = value;
	}
	
	public List<PartnerDTO> getPartners() {
		return partners;
	}
	
	public void setPartners(List<PartnerDTO> partners) {
		this.partners = partners;
	}
		
	public void setViewAllAllowed(boolean value) {
		set("viewAllAllowed", value);
	}

    /**
     * @return  true if the client receiving the DTO is authorized to view data
     * from all partners in this UserDatabase
     */
	public boolean isViewAllAllowed() {
		return (Boolean)get("viewAllAllowed");
	}

	public void setEditAllowed(boolean value) {
		set("editAllowed", value);		
	}

    /**
     * @return true if the client receiving the DTO is authorized to edit data
     * for their Partner in this UserDatabase
     */
	public boolean isEditAllowed() {
		return (Boolean)get("editAllowed");
	}

	public void setDesignAllowed(boolean value) {
		set("designAllowed", value);                                     
	}

    /**
     * @return true if the client receiving the DTO is authorized to design (change indicators, etc)
     * this UserDatabase
     */
	public boolean isDesignAllowed() {
		return (Boolean)get("designAllowed");
	}

	public void setEditAllAllowed(boolean value) {
		set("editAllAllowed", value);
	}

    /**
     * @return true if the client receiving the DTO is authorized to edit data for all Partners
     * in this UserDatabase
     */
	public boolean isEditAllAllowed() {
		return (Boolean)get("editAllAllowed");
	}

    public boolean isManageUsersAllowed() {
        return (Boolean) get("manageUsersAllowed");
    }

    public void setManageUsersAllowed(boolean allowed) {
        set("manageUsersAllowed", allowed);
    }

    public boolean isManageAllUsersAllowed() {
        return (Boolean)get("manageAllUsersAllowed");
    }

    public void setManageAllUsersAllowed(boolean allowed) {
        set("manageAllUsersAllowed", allowed);
    }

    /**
     * @return the Partner of the UserDatabase to which the client belongs
     */
    public PartnerDTO getMyPartner() {
		return getPartnerById(getMyPartnerId());
	}

    /**
     * @return the id of the Partner to which the client belongs
     */
    public int getMyPartnerId() {
        return (Integer) get("myPartnerId");
    }

    public void setMyPartnerId(int id) {
        set("myPartnerId",id);
    }

    /**
     * @return true if the client owns this UserDatabase
     */
	public boolean getAmOwner() {
		return (Boolean)get("amOwner");
	}
	
	public void setAmOwner(boolean value) {
		set("amOwner", value);
	}

    public String getEntityName() {
        return "UserDatabase";
    }

    public PartnerDTO getPartnerById(int id) {
        for(PartnerDTO partner : getPartners()){
            if(partner.getId() == id) {
                return partner;
            }
        }
        return null;
    }

}
