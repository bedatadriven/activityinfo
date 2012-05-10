package org.activityinfo.shared.dto;

import java.util.HashSet;
import java.util.Set;

import org.activityinfo.shared.dto.LockedPeriodDTO.HasLockedPeriod;

import com.extjs.gxt.ui.client.data.BaseModelData;

public final class ProjectDTO 
	extends 
		BaseModelData 
	implements 
		DTO,
		HasLockedPeriod {
	
    private Set<LockedPeriodDTO> lockedPeriods = new HashSet<LockedPeriodDTO>(0);
    private UserDatabaseDTO userDatabase;
    
    public final static String ENTITY_NAME = "Project";

    public ProjectDTO() {
		super();
	}

	public ProjectDTO(int id, String name) {
		super();
		
		setId(id);
		setName(name);
	} 

	@Override
	public int getId() {
		return (Integer)get("id");
	}
	
	public void setId(int id) {
		set("id", id); 
	}
	
	@Override
	public String getName() {
		return (String)get("name");
	}
	
	public void setName(String name) {
		set("name", name);
	}
	
	public void setDescription(String description) {
		set("description", description);
	}

	public String getDescription() {
		return (String)get("description");
	}
	
	@Override
	public Set<LockedPeriodDTO> getLockedPeriods() {
		return lockedPeriods;
	}
	
	public void setLockedPeriods(Set<LockedPeriodDTO> lockedPeriods) {
		this.lockedPeriods = lockedPeriods;
	}

	public void setUserDatabase(UserDatabaseDTO database) {
		this.userDatabase = database;
	}

	public UserDatabaseDTO getUserDatabase() {
		return userDatabase;
	}
	
	@Override
	public Set<LockedPeriodDTO> getEnabledLockedPeriods() {
	    Set<LockedPeriodDTO> enabled = new HashSet<LockedPeriodDTO>(0);
	    for (LockedPeriodDTO lockedPeriod : getLockedPeriods()) {
	    	if (lockedPeriod.isEnabled()) {
	    		enabled.add(lockedPeriod);
	    	}
	    }
	    return enabled;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}
}