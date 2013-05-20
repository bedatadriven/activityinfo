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
 * One-to-One DTO for the {@link org.activityinfo.shared.dto.AttributeDTO}
 * domain object
 * 
 * @author Alex Bertram
 */
@JsonAutoDetect(JsonMethod.NONE)
public final class AttributeDTO extends BaseModelData implements EntityDTO {

    public static final String PROPERTY_PREFIX = "ATTRIB";
    public static final int NAME_MAX_LENGTH = 50;

    public AttributeDTO() {

    }

    public AttributeDTO(AttributeDTO model) {
        super(model.getProperties());

    }

    public AttributeDTO(int id, String name) {
        setId(id);
        setName(name);
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

    public void setName(String value) {
        set("name", value);
    }

    @Override
    @JsonProperty
    @JsonView(DTOViews.Schema.class)
    public String getName()
    {
        return get("name");
    }

    public static String getPropertyName(int attributeId) {
        return PROPERTY_PREFIX + attributeId;
    }

    public static String getPropertyName(AttributeDTO attribute) {
        return getPropertyName(attribute.getId());
    }

    public String getPropertyName() {
        return getPropertyName(getId());
    }

    public static int idForPropertyName(String property) {
        return Integer.parseInt(property.substring(PROPERTY_PREFIX.length()));
    }

    @Override
    public String getEntityName() {
        return "Attribute";
    }

    @Override
    public String toString() {
        return getName();
    }
}
