package org.activityinfo.shared.dto;

import java.util.*;


public class UserDatabaseDTO extends SchemaModel implements EntityDTO {


	public static final int MODEL_TYPE = 14;

	private CountryModel country;
	
	private List<PartnerModel> partners = new ArrayList<PartnerModel>(0);
	private List<ActivityModel> activities = new ArrayList<ActivityModel>(0);


    public UserDatabaseDTO() {
		
	}

    public UserDatabaseDTO(int id, String name) {
        setId(id);
        setName(name);
    }


    public void setVersion(int version) {
		set("version", version);
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

	public List<ActivityModel> getActivities() 
	{
		return activities;
	}
	
	public void setActivities(List<ActivityModel> activities) 
	{
		this.activities = activities;
	}
	
	public CountryModel getCountry()
	{
		return country;
	}

	
	public void setCountry(CountryModel value)
	{
		country = value;
	}
	
	public List<PartnerModel> getPartners()
	{
		return partners;
	}
	
	public void setPartners(List<PartnerModel> partners) {
		this.partners = partners;
	}
		
	public void setViewAllAllowed(boolean value) {
		set("viewAllAllowed", value);
	}
	
	public boolean isViewAllAllowed() {
		return (Boolean)get("viewAllAllowed");
	}
	
	public void setEditAllowed(boolean value) {
		set("editAllowed", value);		
	}

	public boolean isEditAllowed() {
		return (Boolean)get("editAllowed");
	}
	
	public void setDesignAllowed(boolean value) {
		set("designAllowed", value);                                     
	}
	
	public boolean isDesignAllowed() {
		return (Boolean)get("designAllowed");
	}

	public void setEditAllAllowed(boolean value) {
		set("editAllAllowed", value);
	}

	public boolean isEditAllAllowed() {
		return (Boolean)get("editAllAllowed");
	}

    public PartnerModel getMyPartner() {
		return getPartnerById(getMyPartnerId());
	}

    public int getMyPartnerId() {
        return (Integer) get("myPartnerId");
    }

    public void setMyPartnerId(int id) {
        set("myPartnerId",id);
    }
	
	public boolean getAmOwner() {
		return (Boolean)get("amOwner");
	}
	
	public void setAmOwner(boolean value) {
		set("amOwner", value);
	}


	public List<String> getSortedActivityCategoryList() {
		
		Set<String> categories = new HashSet<String>();
		
		for(ActivityModel activity : getActivities()) {
			if(activity.getCategory()!=null) {
				categories.add(activity.getCategory());
			}
		}
		
		List<String> list = new ArrayList<String>(categories);
		Collections.sort(list);
		
		return list;
	}

    public boolean hasIndicators() {

        for(ActivityModel activity : getActivities()) {
            if(activity.getIndicators().size() != 0)
                return true;
        }
        return false;

    }

    public String getEntityName() {
        return "UserDatabase";
    }

    public PartnerModel getPartnerById(int id) {
        for(PartnerModel partner : getPartners()){
            if(partner.getId() == id)
                return partner;
        }
        return null;
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
}
