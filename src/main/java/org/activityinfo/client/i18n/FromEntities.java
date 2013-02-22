package org.activityinfo.client.i18n;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.shared.report.model.DimensionType;

public class FromEntities {

    public String getDimensionTypePluralName(DimensionType dimension) {
        switch (dimension) {
        case Activity:
            return removeSpaces(I18N.CONSTANTS.activities());
        case AdminLevel:
            return removeSpaces(I18N.CONSTANTS.adminEntities());
        case Partner:
            return removeSpaces(I18N.CONSTANTS.partners().trim());
        case Project:
            return removeSpaces(I18N.CONSTANTS.projects());
        case AttributeGroup:
            return removeSpaces(I18N.CONSTANTS.attributeTypes());
        case Indicator:
            return removeSpaces(I18N.CONSTANTS.indicators());
        case Target:
            return removeSpaces(I18N.CONSTANTS.targets());
        case Site:
            return removeSpaces(I18N.CONSTANTS.sites());
        case Database:
            return removeSpaces(I18N.CONSTANTS.databases());
        case Location:
            return removeSpaces(I18N.CONSTANTS.locations());
        }
        return "No pluralized string definition for " + dimension.toString();
    }

    private static String removeSpaces(String stringWithSpaces) {
        return stringWithSpaces.replaceAll(" ", "").toLowerCase();
    }
}
