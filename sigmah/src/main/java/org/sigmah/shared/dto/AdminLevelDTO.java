/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;


/**
 * One-to-one DTO for the {@link org.sigmah.server.domain.AdminLevel} domain object.
 *
 * @author Alex Bertram
 */
public final class AdminLevelDTO extends BaseModelData implements DTO {
	public static final String PROPERTY_PREFIX = "E";

	public AdminLevelDTO(){
	}

    public AdminLevelDTO(int id, String name) {
        setId(id);
        setName(name);
    }

    public AdminLevelDTO(int id, int parentId, String name) {
        setId(id);
        setParentLevelId(parentId);
        setName(name);
    }

    public void setId(int id) {
		 set("id", id);
	}
	
	public int getId() {
		return (Integer)get("id");
	}

	public void setName(String name) {
		set("name", name);
	}
	
	public String getName() {
		return get("name");		
	}
	
	public Integer getParentLevelId() {
		return get("parentLevelId");
	}
	
	public void setParentLevelId(Integer value) {
		set("parentLevelId", value);
	}
	
	public boolean isRoot() {
		return get("parentLevelId") == null;
	}

    /**
	 * 
	 * @param levelId
	 * @return The name of the property in a Site/Location object of the given level
	 */
	public static String getPropertyName(int levelId) { 
		return PROPERTY_PREFIX + levelId;
	}
	
	public String getPropertyName() {
		return getPropertyName(this.getId());
	}

	public static int levelIdForProperty(String field) {
		return Integer.parseInt(field.substring(AdminLevelDTO.PROPERTY_PREFIX.length()));
	}

}
