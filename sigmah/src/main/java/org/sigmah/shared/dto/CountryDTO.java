/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;


import com.extjs.gxt.ui.client.data.BaseModelData;

import java.util.ArrayList;
import java.util.List;

/**
 * One-to-one DTO for {@link org.sigmah.shared.domain.Country} domain objects.
 *
 * @author Alex Bertram
 */
public final class CountryDTO extends BaseModelData implements DTO {

	private List<AdminLevelDTO> adminLevels = new ArrayList<AdminLevelDTO>(0);
	private List<LocationTypeDTO> locationTypes = new ArrayList<LocationTypeDTO>(0);
    private BoundingBoxDTO bounds;

    
	public CountryDTO()
	{
	}

    public CountryDTO(int id, String name) {
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
	
	public List<AdminLevelDTO> getAdminLevels() {
		return this.adminLevels;
	}
	
	public void setAdminLevels(List<AdminLevelDTO> levels) {
		this.adminLevels = levels;
	}
	
	public List<LocationTypeDTO> getLocationTypes() {
		return this.locationTypes;
	}
	
	public void setLocationTypes(List<LocationTypeDTO> types) {
		this.locationTypes = types;
	}

    public BoundingBoxDTO getBounds() {
        return bounds;
    }

    public void setBounds(BoundingBoxDTO bounds) {
        this.bounds = bounds;
    }

    /**
     * Finds an AdminEntity by id
     *
     * @param levelId the id of the AdminEntity to return
     * @return the AdminEntity with corresponding id or null if no such AdminEntity is found in the list
     */
	public AdminLevelDTO getAdminLevelById(int levelId) {
		for(AdminLevelDTO level : this.adminLevels) {
			if(level.getId() == levelId) {
				return level;
			}
		}
		return null;
	}

    /**
     * Returns a list of <code>AdminLevelDTO</code>s that are ancestors of the
     * the AdminLevel with an id of <code>levelId</code>  in order descending from the root.
     *
     * @param levelId the id of AdminLevel
     * @return a list of AdminLevelDTOs in <code>adminLevels</code>  which are ancestors of
     * the AdminLevel with the id of <code>levelId</code>, or null if no AdminLevelDTO with the
     * given id or exists or if the indicated AdminLevel is a root level.
     */
	public List<AdminLevelDTO> getAdminLevelAncestors(int levelId) {
		List<AdminLevelDTO> ancestors = new ArrayList<AdminLevelDTO>();
		
		AdminLevelDTO level = getAdminLevelById(levelId);
		
		if(level == null) {
            return null;
        }
		
		while(true) {
			ancestors.add(0, level);
			
			if(level.isRoot()) {
                return ancestors;
            } else {
                level = getAdminLevelById(level.getParentLevelId());
            }
		}
	}

    /**
     * Returns the <code>LocationTypeDTO</code> with the given <code>locationTypeId</code>
     * @param locationTypeId the id of a <code>LocationTypeDTO</code> in <code>locationTypes</code>
     * @return the <code>LocationTypeDTO</code> in <code>locationTypes</code> with the id <code>locationTypeId</code>
     */
    public LocationTypeDTO getLocationTypeById(int locationTypeId) {
        for(LocationTypeDTO type : getLocationTypes()) {
            if(type.getId()==locationTypeId) {
                return type;
            }
        }
        return null;
    }
}
