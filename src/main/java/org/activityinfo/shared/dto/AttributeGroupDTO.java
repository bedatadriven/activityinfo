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

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonView;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * One-to-one DTO for the
 * {@link org.activityinfo.server.database.hibernate.entity.AttributeGroup}
 * domain object
 * 
 */
@JsonAutoDetect(JsonMethod.NONE)
public final class AttributeGroupDTO extends BaseModelData implements EntityDTO {

    public static final int NAME_MAX_LENGTH = 255;

    private List<AttributeDTO> attributes = new ArrayList<AttributeDTO>(0);

    public AttributeGroupDTO() {
    }

    /**
     * Creates a shallow clone
     * 
     * @param model
     */
    public AttributeGroupDTO(AttributeGroupDTO model) {
        super(model.getProperties());
        setAttributes(model.getAttributes());
    }

    public boolean isEmpty() {
        return this.attributes == null || attributes.isEmpty();
    }

    public AttributeGroupDTO(int id) {
        this.setId(id);
    }

    @Override
    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    public void setName(String name) {
        set("name", name);
    }

    @Override
    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public String getName() {
        return get("name");
    }

    public List<AttributeDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDTO> attributes) {
        this.attributes = attributes;
    }

    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public boolean isMultipleAllowed() {
        return (Boolean) get("multipleAllowed");
    }

    public void setMultipleAllowed(boolean allowed) {
        set("multipleAllowed", allowed);
    }

    @Override
    public String getEntityName() {
        return "AttributeGroup";
    }
}
