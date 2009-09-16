package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;


public class AdminEntityModel extends BaseModelData implements DTO {

	private Bounds bounds;
	
	public AdminEntityModel() {
		
	}

    public AdminEntityModel(int levelId, int id, String name) {
        setId(id);
        setName(name);
        setLevelId(levelId);
    }

    public AdminEntityModel(int levelId, int id, int parentId, String name) {
        setId(id);
        setParentId(parentId);
        setName(name);
        setLevelId(levelId);
    }

    public AdminEntityModel(int levelId, int id, String name, Bounds bounds) {
        setId(id);
        setName(name);
        setLevelId(levelId);
        setBounds(bounds);
    }

    public AdminEntityModel(int levelId, int id, int parentId, String name, Bounds bounds) {
        setId(id);
        setLevelId(levelId);
        setParentId(parentId);
        setName(name);
        setBounds(bounds);
    }


    public void setId(int id) {
		set("id", id);
	}
	
	public int getId() {
		return (Integer)get("id");
	}
	
	public String getName() {
		return get("name");
	}

	public void setName(String value) {
		set("name", value);
	}

	public int getLevelId() {
		return (Integer)get("levelId");
	}

	public void setLevelId(int value) {
		set("levelId", value);
	}

	public void setParentId(Integer value) {
		set("parentId", value);
	}
	
	public Integer getParentId() { 
		return get("parentId");
	}

	public boolean hasBounds() {
		return getBounds() != null;
	}
	
	public Bounds getBounds() {
		return bounds;
	}
	
	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}
	
	public static String getPropertyName(int levelId) {
		return AdminLevelModel.getPropertyName(levelId);
	}
	
	public String getPropertyName() {
		return AdminLevelModel.getPropertyName(this.getLevelId());
	}

	public boolean isParentIdNull() {
		return get("parentId") == null;
	}
	
	@Override
	public String toString() {
		return getName();
	}

    @Override
    public int hashCode() {
        return get("id").hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if(other==null)
            return false;
        if(other==this)
            return true;
        if(!(other instanceof AdminEntityModel))
            return false;

        AdminEntityModel that = (AdminEntityModel)other;

        return getId() == that.getId();


    }
}
