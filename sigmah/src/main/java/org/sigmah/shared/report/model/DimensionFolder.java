package org.sigmah.shared.report.model;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class DimensionFolder extends BaseModelData {

	private DimensionType type;

    public DimensionFolder(String name, DimensionType type, String id) {
    	set("name", name);
    	this.type= type;
    	set("id",id);
    }
  
    public DimensionType getType() {
		return type;
	}

	public void setType(DimensionType type) {
		this.type = type;
	}
}
