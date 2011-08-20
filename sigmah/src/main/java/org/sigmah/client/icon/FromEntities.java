package org.sigmah.client.icon;

import org.sigmah.shared.report.model.DimensionType;

import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class FromEntities {
	/*
	 * Returns an icon from given DimensionType or defaults to unknown icon if 
	 * DimensionType is not supported
	 */
	public AbstractImagePrototype fromDimension(DimensionType dimension) {
		switch(dimension) {
		case AdminLevel:
			return IconImageBundle.ICONS.adminlevel1();
		case Database:
			return IconImageBundle.ICONS.database();
		case Activity:
			return IconImageBundle.ICONS.activity();
		case Project:
			return IconImageBundle.ICONS.project();
		case Partner:
			return IconImageBundle.ICONS.partner();
		case Indicator:
			return IconImageBundle.ICONS.indicator();
		case Site:
			return IconImageBundle.ICONS.site();
		}
		
		return IconImageBundle.ICONS.none();
	}
}
