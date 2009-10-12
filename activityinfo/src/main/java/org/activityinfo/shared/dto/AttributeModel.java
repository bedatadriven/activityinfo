package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModel;

public class AttributeModel extends BaseModel implements EntityDTO {

    public static final String PROPERTY_PREFIX = "ATTRIB";


    public AttributeModel()
	{
		
	}

    public AttributeModel(AttributeModel model) {
        super(model.getProperties());
        
    }

    public int getId() {
        return (Integer)get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    public void setVersion(int version) {
		set("version", version);
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
	
	public static String getPropertyName(AttributeModel attribute) {
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
