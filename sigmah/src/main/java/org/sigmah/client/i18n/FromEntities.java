package org.sigmah.client.i18n;

import org.sigmah.shared.report.model.DimensionType;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class FromEntities {
	private static BiMap<String, DimensionType> map = HashBiMap.<String, DimensionType>create();
	
	static {
		map.put(removeSpaces(I18N.CONSTANTS.activity()), DimensionType.Activity);
		map.put(removeSpaces(I18N.CONSTANTS.project()), DimensionType.Project);
		map.put(removeSpaces(I18N.CONSTANTS.partner()), DimensionType.Partner);
		map.put(removeSpaces(I18N.CONSTANTS.database()), DimensionType.Database);
		map.put(removeSpaces(I18N.CONSTANTS.administrativeLevel()), DimensionType.AdminLevel);
		map.put(removeSpaces(I18N.CONSTANTS.indicator()), DimensionType.Indicator);
		map.put(removeSpaces(I18N.CONSTANTS.location()), DimensionType.Location);
		map.put(removeSpaces(I18N.CONSTANTS.attributeGroup()), DimensionType.AttributeGroup);
		map.put(removeSpaces(I18N.CONSTANTS.site()), DimensionType.Site);
	}
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
	
	public DimensionType fromLocalizedString(String dimensionString) {
		return map.get(dimensionString);
	}
	
	public String localizedStringFrom(DimensionType dimension) {
		return map.inverse().get(dimension);
	}
	
	private static String removeSpaces(String stringWithSpaces) {
		return stringWithSpaces.replaceAll(" ", "").toLowerCase();
	}
}
