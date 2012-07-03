package org.activityinfo.client.report.editor.pivotTable;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.AttributeGroupDimension;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.data.BaseModelData;


public class DimensionModel extends BaseModelData {

	private final Dimension dimension;
	
	public DimensionModel(String name) {
		setName(name);
		this.dimension = null;
	}
		
	public DimensionModel(Dimension dimension, String name) {
		super();
		this.dimension = dimension;
		setName(name);
	}
	
	public DimensionModel(DimensionType type, String name ) {
		this.dimension = new Dimension(type);
		setName(name);
	}
	
	public DimensionModel(DateUnit unit) {
		this.dimension = new DateDimension(unit);
		switch (unit) {
		case YEAR:
			setName(I18N.CONSTANTS.year());
			break;

		case QUARTER:
			setName(I18N.CONSTANTS.quarter());
			break;
			
		case MONTH:
			setName(I18N.CONSTANTS.month());
			break;
		case WEEK_MON:
			setName(I18N.CONSTANTS.weekMon());
			break;
		default:
			throw new IllegalArgumentException(unit.name());
		}
	}

	public DimensionModel(AdminLevelDTO level) {
		this.dimension = new AdminDimension(level.getId());
		setName(level.getName());
	}

	public DimensionModel(AttributeGroupDTO attributeGroup) {
		this.dimension  = new AttributeGroupDimension(attributeGroup.getId());
		setName(attributeGroup.getName());
	}

	public String getCaption() {
		return get("name");
	}
	
	public void setName(String caption) {
		set("name", caption);
	}

	public Dimension getDimension() {
		return dimension;
	}

	public boolean hasDimension() {
		return dimension != null;
	}
}
