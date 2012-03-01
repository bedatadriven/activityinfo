package org.sigmah.client.i18n;

import org.sigmah.shared.report.model.DimensionType;

public class FromEntities {

	public String getDimensionTypePluralName(DimensionType dimension) {
		switch (dimension) {
			case Activity: return removeSpaces(I18N.CONSTANTS.activities());
			case AdminLevel: return removeSpaces(I18N.CONSTANTS.adminEntities());
			case Partner: return removeSpaces(I18N.CONSTANTS.partners().trim());
			case Project: return removeSpaces(I18N.CONSTANTS.projects());
			case AttributeGroup: return removeSpaces(I18N.CONSTANTS.attributeTypes());
			case Indicator: return removeSpaces(I18N.CONSTANTS.indicators());
			case Site: return removeSpaces(I18N.CONSTANTS.sites());
			case Database: return removeSpaces(I18N.CONSTANTS.databases());
			case Location: return removeSpaces(I18N.CONSTANTS.locations());
		}
		return "No pluralized string definition for " + dimension.toString();
	}
	
	private static String removeSpaces(String stringWithSpaces) {
		return stringWithSpaces.replaceAll(" ", "").toLowerCase();
	}
}
