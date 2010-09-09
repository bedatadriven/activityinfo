/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModel;

import java.util.ArrayList;
import java.util.List;


/**
 * One-to-one DTO of the {@link org.sigmah.shared.domain.UserDatabase} domain object.
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

    /**
     * @return  this UserDatabase's id
     */
    public int getId() {
        return (Integer)get("id");
    }

    /**
     * Sets this UserDatabase's id
     */
    public void setId(int id) {
        set("id", id);
    }

    /**
     * 
     * @return the name of this UserDatabase
     */
    public String getName() {
		return get("name");
	}

    /**
     * Sets the name of this UserDatabase
     */
	public void setName(String name) {
		set("name", name);
	}

    /**
     * Sets the name of this UserDatabase's owner
     * @param ownerName
     */
	public void setOwnerName(String ownerName) {
		set("ownerName", ownerName);
	}

    /**
     * 
     * @return the name of this UserDatabase's owner
     */
	public String getOwnerName() {
		return get("ownerName");
	}

    /**
     * Sets the email of this UserDatabase's owner
     */
	public void setOwnerEmail(String ownerEmail) { 
		set("ownerEmail", ownerEmail);
	}

    /**
     * @return the email of this UserDatabase's owner
     */
	public String getOwnerEmail() {
		return get("ownerEmail");
	}

    /**
     * Sets the full, descriptive name of this UserDatabase
     * 
     */
	public void setFullName(String fullName) {
		set("fullName", fullName);
	}

    /**
     * Gets the full, descriptive name of this UserDatabase
     */
	public String getFullName() {
		return get("fullName");
	}

    /**
     * @return this list of ActivityDTOs that belong to this UserDatabase
     */
	public List<ActivityDTO> getActivities() {
		return activities;
	}

    /**
     * @param activities  sets the list of Activities in this UserDatabase
     */
	public void setActivities(List<ActivityDTO> activities) {
		this.activities = activities;
	}

    /**
     * 
     * @return the Country in which this UserDatabase is set
     */
	public CountryDTO getCountry() 	{
		return country;
	}

    /**
     * Sets the Country to which this UserDatabase belongs 
     */
	public void setCountry(CountryDTO country) {
		this.country = country;
	}

    /**
     * @return the list of Partners who belong to this UserDatabase
     */
	public List<PartnerDTO> getPartners() {
		return partners;
	}

    /**
     * Sets the list of Partners who belong to this UserDatabase
     */
	public void setPartners(List<PartnerDTO> partners) {
		this.partners = partners;
	}

    /**
     * Sets the permission of the current user to view all partner's data in this UserDatabase.
     * See {@link org.sigmah.shared.domain.UserPermission#setAllowViewAll(boolean)}
     */
	public void setViewAllAllowed(boolean value) {
		set("viewAllAllowed", value);
	}

    /**
     * @return  true if the client receiving the DTO is authorized to view data
     * from all partners in this UserDatabase.
     * See {@link org.sigmah.shared.domain.UserPermission#setAllowViewAll(boolean)}
     */
	public boolean isViewAllAllowed() {
		return (Boolean)get("viewAllAllowed");
	}

    /**
     * Sets the permission of the current user to edit data on behalf of the Partner
     * in this UserDatabase to which the current user belongs.
     * 
     */
	public void setEditAllowed(boolean allowed) {
		set("editAllowed", allowed);		
	}

    /**
     * @return true if the client receiving the DTO is authorized to edit data
     * for their Partner in this UserDatabase
     */
	public boolean isEditAllowed() {
		return (Boolean)get("editAllowed");
	}

    /**
     * Sets the permission of the current user to design this UserDatabase. See
     * {@link org.sigmah.shared.domain.UserPermission#setAllowDesign(boolean)}
     */
	public void setDesignAllowed(boolean allowed) {
		set("designAllowed", allowed);
	}

    /**
     * @return true if the client receiving the DTO is authorized to design (change indicators, etc)
     * this UserDatabase
     */
	public boolean isDesignAllowed() {
		return (Boolean)get("designAllowed");
	}

    /**
     * Sets the permission of the current user to edit data in this UserDatabase on behalf of all
     * partners.
     */
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

    /**
     * @return true if current user is allowed to make changes to user permissions on behalf of the
     * Partner to which they belong
     */
    public boolean isManageUsersAllowed() {
        return (Boolean) get("manageUsersAllowed");
    }

    /**
     * Sets the permission of the current user to make changes to user permissions on behalf of the
     * Partner to which they belong in this UserDatabase.
     *
     */
    public void setManageUsersAllowed(boolean allowed) {
        set("manageUsersAllowed", allowed);
    }

    /**
     * @return true if the current user is allowed to make changes to user permissions on behalf of all
     * Partners in this UserDatabase
     */
    public boolean isManageAllUsersAllowed() {
        return (Boolean)get("manageAllUsersAllowed");
    }

    /**
     * Sets the permission of the current user to modify user permissions for this UserDatabase on behalf
     * of all Partners in this UserDatabase
     */
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

    /**
     *  Sets the id of the Partner to which the current user belongs
     */
    public void setMyPartnerId(int partnerId) {
        set("myPartnerId",partnerId);
    }

    /**
     * @return true if the client owns this UserDatabase
     */
	public boolean getAmOwner() {
		return (Boolean)get("amOwner");
	}

    /**
     * Sets the flag to determine whether the current user is the owner of this database.
     */
	public void setAmOwner(boolean value) {
		set("amOwner", value);
	}

    public String getEntityName() {
        return "UserDatabase";
    }

    /**
     * Searches this UserDatabase's list of Partners for the PartnerDTO with the given
     * id.
     * 
     * @return  the matching UserDatabaseDTO or null if no matches
     */
    public PartnerDTO getPartnerById(int id) {
        for(PartnerDTO partner : getPartners()){
            if(partner.getId() == id) {
                return partner;
            }
        }
        return null;
    }
}
