package org.sigmah.client.i18n;

import org.sigmah.shared.report.model.DimensionType;

public class FromEntities {
	public String getDimensionTypePluralName(DimensionType dimension) {
		switch (dimension) {
		case Activity:
			return I18N.CONSTANTS.activities();
		case AdminLevel:
			return I18N.CONSTANTS.adminEntities();
		case Partner:
			return I18N.CONSTANTS.partners();
		case Project:
			return I18N.CONSTANTS.projects();
		case AttributeGroup:
			return I18N.CONSTANTS.attributeTypes();
		case Indicator:
			return I18N.CONSTANTS.indicators();
		case Site:
			return I18N.CONSTANTS.sites();
		}
		return "No pluralized string definition for " + dimension.toString();
	}
}
