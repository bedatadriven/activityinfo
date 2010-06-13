package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;


/**
 * One-to-one DTO for the {@link org.activityinfo.server.domain.AdminEntity} domain object.
 *
 * @author Alex Bertram
 */
public final class AdminEntityDTO extends BaseModelData implements DTO {
	private BoundingBoxDTO bounds;
	
	public AdminEntityDTO() {
	}

    public AdminEntityDTO(int levelId, int id, String name) {
        setId(id);
        setName(name);
        setLevelId(levelId);
    }

    public AdminEntityDTO(int levelId, int id, int parentId, String name) {
        setId(id);
        setParentId(parentId);
        setName(name);
        setLevelId(levelId);
    }

    public AdminEntityDTO(int levelId, int id, String name, BoundingBoxDTO bounds) {
        setId(id);
        setName(name);
        setLevelId(levelId);
        setBounds(bounds);
    }

    public AdminEntityDTO(int levelId, int id, int parentId, String name, BoundingBoxDTO bounds) {
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

    /**
     * 
     * @return  the id of this AdminEntity's corresponding {@link org.activityinfo.server.domain.AdminLevel}
     */
	public int getLevelId() {
		return (Integer)get("levelId");
	}

	public void setLevelId(int value) {
		set("levelId", value);
	}

	public void setParentId(Integer value) {
		set("parentId", value);
	}

    /**
     *
     * @return the id of this AdminEntity's corresponding parent AdminEntity
     */
	public Integer getParentId() { 
		return get("parentId");
	}

	public boolean hasBounds() {
		return getBounds() != null;
	}

    /**
     *
     * @return bounding
     */
	public BoundingBoxDTO getBounds() {
		return bounds;
	}
	
	public void setBounds(BoundingBoxDTO bounds) {
		this.bounds = bounds;
	}
	
	public static String getPropertyName(int levelId) {
		return AdminLevelDTO.getPropertyName(levelId);
	}
	
	public String getPropertyName() {
		return AdminLevelDTO.getPropertyName(this.getLevelId());
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
        if(!(other instanceof AdminEntityDTO))
            return false;

        AdminEntityDTO that = (AdminEntityDTO)other;

        return getId() == that.getId();
    }
}
