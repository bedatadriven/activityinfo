package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * One-to-One DTO for the {@link org.activityinfo.shared.dto.AttributeDTO} domain object
 *
 * @author Alex Bertram
 */
public final class AttributeDTO extends BaseModelData implements EntityDTO {

    public static final String PROPERTY_PREFIX = "ATTRIB";

    public AttributeDTO() {
		
	}

    public AttributeDTO(AttributeDTO model) {
        super(model.getProperties());
        
    }

    public AttributeDTO(int id, String name) {
        setId(id);
        setName(name);
    }

    public int getId() {
        return (Integer)get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    public void setName(String value) {
		set("name", value);
	}
	
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

    public String getEntityName() {
        return "Attribute";
    }
}
