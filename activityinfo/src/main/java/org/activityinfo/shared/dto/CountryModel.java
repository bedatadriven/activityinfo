package org.activityinfo.shared.dto;


import com.extjs.gxt.ui.client.data.BaseModelData;

import java.util.ArrayList;
import java.util.List;

public class CountryModel extends BaseModelData implements DTO {

	private List<AdminLevelModel> adminLevels = new ArrayList<AdminLevelModel>(0);
	
	private List<LocationTypeModel> locationTypes = new ArrayList<LocationTypeModel>(0);

    private Bounds bounds;

	public CountryModel()
	{
	}

    public CountryModel(int id, String name) {
        setId(id);
        setName(name);
    }

    public void setId(int id) {
		set("id",id);
	}
	
	public int getId() {
		return (Integer)get("id");
	}
	
	public String getName()
	{
		return get("name");		
	}
	
	public void setName(String value) {
		set("name", value);
	}
	
	public List<AdminLevelModel> getAdminLevels() {
		return this.adminLevels;
	}
	
	public void setAdminLevels(List<AdminLevelModel> levels) {
		this.adminLevels = levels;
	}
	
	public List<LocationTypeModel> getLocationTypes() {
		return this.locationTypes;
	}
	
	public void setLocationTypes(List<LocationTypeModel> types) {
		this.locationTypes = types;
	}

	public AdminLevelModel getAdminLevelById(int levelId) {
		for(AdminLevelModel level : this.adminLevels) {
			if(level.getId() == levelId) {
				return level;
			}
		}
		
		return null;
	}
	
	public List<AdminLevelModel> getAncestors(int levelId) {
		List<AdminLevelModel> ancestors = new ArrayList<AdminLevelModel>();
		
		AdminLevelModel level = getAdminLevelById(levelId);
		
		if(level == null)
			return null;
		
		while(true) {
			
			ancestors.add(0, level);
			
			if(level.isRoot())
				return ancestors;
			else 
				level = getAdminLevelById(level.getParentLevelId());
		}
		
	}

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public LocationTypeModel getLocationTypeById(int id) {
        for(LocationTypeModel type : getLocationTypes()) {
            if(type.getId()==id)
                return type;
        }
        return null;
    }
}
