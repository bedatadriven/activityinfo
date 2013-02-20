/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.dto;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonView;

import com.extjs.gxt.ui.client.data.BaseModelData;


/**
 * One-to-one DTO of the {@link org.activityinfo.server.database.hibernate.entity.LocationType LocationType}
 * domain object.
 *
 * @author Alex Bertram
 */
public final class LocationTypeDTO extends BaseModelData implements DTO {

	public LocationTypeDTO() {
	}

    public LocationTypeDTO(int id, String name) {
        setId(id);
        setName(name);
    }

    @JsonProperty
	@JsonView(DTOViews.Schema.class)
    public void setId(int id) {
		set("id", id);
	}
	
	public int getId() {
		return (Integer)get("id");
	}
	
	public void setName(String value) {
		set("name", value);
	}
	
    @JsonProperty
	@JsonView(DTOViews.Schema.class)
	public String getName() { 
		return get("name");
	}
	
    @JsonProperty("adminLevelId")
	@JsonView(DTOViews.Schema.class)
	public Integer getBoundAdminLevelId() {
		return get("boundAdminLevelId");
	}
	
	public void setBoundAdminLevelId(Integer id) {
		set("boundAdminLevelId", id);
	}

	public boolean isAdminLevel() {
		return getBoundAdminLevelId() != null;
	}

}
