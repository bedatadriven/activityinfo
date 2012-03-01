package org.sigmah.client.page.entry.grouping;

import org.sigmah.client.i18n.I18N;

import com.extjs.gxt.ui.client.data.BaseModelData;

class GroupingModelData extends BaseModelData {
	private GroupingModel model;
	
	public static final GroupingModelData NONE = new GroupingModelData(I18N.CONSTANTS.none(), NullGroupingModel.INSTANCE);
 
	public static final GroupingModelData TIME = new GroupingModelData(I18N.CONSTANTS.yearMonthGrouping(), TimeGroupingModel.INSTANCE);
	
	public GroupingModelData(String label, GroupingModel model) {
		this.model = model;
		set("label", label);
	}
	
	public GroupingModel getModel() {
		return model;
	}
	
}