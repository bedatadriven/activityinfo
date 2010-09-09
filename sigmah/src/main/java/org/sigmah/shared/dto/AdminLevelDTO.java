/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;


/**
 * One-to-one DTO for the {@link org.sigmah.shared.domain.AdminLevel} domain object.
 *
 * @author Alex Bertram
 */
public final class AdminLevelDTO extends BaseModelData implements DTO {
	public static final String PROPERTY_PREFIX = "E";

	public AdminLevelDTO(){
	}

    /**
     *
     * @param id  this AdminLevel's id
     * @param name this AdminLevel's name
     */
    public AdminLevelDTO(int id, String name) {
        super();
        setId(id);
        setName(name);
    }

    public AdminLevelDTO(int id, int parentId, String name) {
        super();
        setId(id);
        setParentLevelId(parentId);
        setName(name);
    }

    /**
     * Sets the id of this AdminLevel
     */
    public void setId(int id) {
		 set("id", id);
	}

    /**
     * @return the id of this AdminLevel
     */
	public int getId() {
		return (Integer)get("id");
	}

    /**
     * Sets the name of this AdminLevel
     */
	public void setName(String name) {
		set("name", name);
	}

    /**
     * @return the name of this AdminLevel
     */
	public String getName() {
		return get("name");		
	}

    /**
     *
     * @return the id of this AdminLevel's parent AdminLevel
     */
	public Integer getParentLevelId() {
		return get("parentLevelId");
	}

    /**
     * Sets the id of this AdminLevel's parent AdminLevel
     */
	public void setParentLevelId(Integer value) {
		set("parentLevelId", value);
	}

    /**
     *
     * @return true if this AdminLevel is s root AdminLevel within it's Country
     */
	public boolean isRoot() {
		return get("parentLevelId") == null;
	}

    /**
	 * Gets the propertyName for the given AdminLevel when stored in pivoted form.
     *
	 * @param levelId
	 * @return The name of the property for this AdminLevel when stored in pivoted form
	 */
	public static String getPropertyName(int levelId) { 
		return PROPERTY_PREFIX + levelId;
	}

    /**
     * @return the propertyName to be used for this AdminLevel when stored in pivoted form
     */
	public String getPropertyName() {
		return getPropertyName(this.getId());
	}

    /**
     * Parses an admin propertyName for the referenced AdminLevel
     *
     * @param propertyName
     * @return
     */
	public static int levelIdForPropertyName(String propertyName) {
		return Integer.parseInt(propertyName.substring(AdminLevelDTO.PROPERTY_PREFIX.length()));
	}

}
