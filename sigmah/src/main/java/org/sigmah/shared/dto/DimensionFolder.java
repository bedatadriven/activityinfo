package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;
import org.sigmah.shared.report.model.DimensionType;

public class DimensionFolder extends BaseModelData {
	
	private DimensionType type;
	private int depth;
	private int id;
	
    public DimensionFolder(String name, DimensionType type, int depth, int id) {
    	this.type = type;
    	this.depth = depth;
    	this.id = id;
    	set("caption", name);
		set("id", "folder_" + type + "_" + depth + "_" + id);
    	set("name", name);
    }
    
	public DimensionType getType() {
		return this.type;
	}

	public void setType(DimensionType type) {
		this.type = type;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
