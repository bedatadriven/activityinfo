/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;


/**
 * One-to-one DTO for the {@link org.sigmah.server.domain.AdminEntity} domain object.
 *
 * @author Alex Bertram
 */
public final class AdminEntityDTO extends BaseModelData implements DTO {
	private BoundingBoxDTO bounds;
	
	public AdminEntityDTO() {
	}

    /**
     *
     * @param levelId the id of the AdminLevel to which this AdminEntity belongs
     * @param id  the id of this AdminEntity
     * @param name  the name of this AdminEntity
     */
    public AdminEntityDTO(int levelId, int id, String name) {
        setId(id);
        setName(name);
        setLevelId(levelId);
    }

    /**
     *
     * @param levelId the id of the AdminLevel to which this AdminEntity belongs
     * @param id the id of this AdminEntity
     * @param parentId the id of this AdminEntity's parent
     * @param name  this AdminEntity's name
     */
    public AdminEntityDTO(int levelId, int id, int parentId, String name) {
        setId(id);
        setParentId(parentId);
        setName(name);
        setLevelId(levelId);
    }

    /**
     * @param levelId the id of the AdminLevel to which this AdminEntity belongs
     * @param id the id of this AdminEntity
     * @param name  the name of this AdminEntity
     * @param bounds the geographing BoundingBox of this AdminEntity
     */
    public AdminEntityDTO(int levelId, int id, String name, BoundingBoxDTO bounds) {
        setId(id);
        setName(name);
        setLevelId(levelId);
        setBounds(bounds);
    }

    /**
     * @param levelId the id of the AdminLevel to which this AdminEntity belongs
     * @param id the id of this AdminEntity
     * @param parentId the id of this AdminEntity's parent
     * @param name  the name of this AdminEntity
     * @param bounds the geographing BoundingBox of this AdminEntity
     */
    public AdminEntityDTO(int levelId, int id, int parentId, String name, BoundingBoxDTO bounds) {
        setId(id);
        setLevelId(levelId);
        setParentId(parentId);
        setName(name);
        setBounds(bounds);
    }


    /**
     * Sets this AdminEntity's id
     */
    public void setId(int id) {
		set("id", id);
	}

    /**
     * @return  this AdminEntity's id
     */
	public int getId() {
		return (Integer)get("id");
	}

    /**
     * @return this AdminEntity's name
     */
	public String getName() {
		return get("name");
	}

    /**
     * Sets this AdminEntity's name
     */
	public void setName(String name) {
		set("name", name);
	}

    /**
     * 
     * @return  the id of this AdminEntity's corresponding {@link org.sigmah.server.domain.AdminLevel}
     */
	public int getLevelId() {
		return (Integer)get("levelId");
	}

    /**
     * Sets the id of the AdminLevel to which this AdminEntity belongs
     */
	public void setLevelId(int levelId) {
		set("levelId", levelId);
	}

    /**
     * Sets the id of this AdminEntity's parent.
     */
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

    /**
     * @return  true if this AdminEntity has non-null bounds
     */
	public boolean hasBounds() {
		return getBounds() != null;
	}

    /**
     *
     * @return the geographic BoundingBoxDTO of this AdminEntity
     */
	public BoundingBoxDTO getBounds() {
		return bounds;
	}

    /**
     * Sets the BoundingBoxDTO of this AdminEntity.
     */
	public void setBounds(BoundingBoxDTO bounds) {
		this.bounds = bounds;
	}

    /**
     * Gets the property name for a given AdminLevel when AdminEntities are stored in pivoted form.
     *
     * @param levelId the id of the AdminLevel
     * @return the property name
     */
	public static String getPropertyName(int levelId) {
		return AdminLevelDTO.getPropertyName(levelId);
	}

    /**
     * @return the property name used for this AdminEntity's AdminLevel when stored in pivoted form
     */
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

    /**
     * Tests for equality based on ID.
     */
    @Override
    public boolean equals(Object other) {
        if(other==null) {
            return false;
        }
        if(other==this) {
            return true;
        }
        if(!(other instanceof AdminEntityDTO)) {
            return false;
        }

        AdminEntityDTO that = (AdminEntityDTO)other;
        return getId() == that.getId();
    }
}
