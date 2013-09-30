package org.activityinfo.shared.dto;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonView;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * One-to-one DTO of the
 * {@link org.activityinfo.server.database.hibernate.entity.LocationType
 * LocationType} domain object.
 * 
 * @author Alex Bertram
 */
@JsonAutoDetect(JsonMethod.NONE)
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
        return (Integer) get("id");
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
