package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModel;

import java.util.ArrayList;
import java.util.List;

public class AttributeGroupModel extends BaseModel implements EntityDTO, Versioned {

	private List<AttributeModel> attributes = new ArrayList<AttributeModel>(0);
	
	public AttributeGroupModel() {
		
	}

    /**
     * Creates a shallow clone
     * @param model
     */
    public AttributeGroupModel(AttributeGroupModel model) {
        super(model.getProperties());
        setAttributes(model.getAttributes());
    }
	
	public AttributeGroupModel(int id) {
		this.setId(id);
	}

    public int getId() {
        return (Integer)get("id");
    }

    public void setId(int id) {
        set("id",  id);
    }

    public Integer getVersion() {
		return get("version");
	}
	
	public void setVersion(int version) {
		set("version", version);
	}
	
	public void setName(String name) {
		set("name", name);
	}
	
	public String getName() {
		return get("name");
	}

	public List<AttributeModel> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(List<AttributeModel> attributes) {
		this.attributes = attributes;		
	}

	public boolean isMultipleAllowed() {
		return (Boolean)get("multipleAllowed");
	}
	
	public void setMultipleAllowed(boolean allowed) {
		set("multipleAllowed", allowed);
	}


    public String getEntityName() {
        return "AttributeGroup";
    }
}
