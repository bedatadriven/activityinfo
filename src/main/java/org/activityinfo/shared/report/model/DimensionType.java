

package org.activityinfo.shared.report.model;

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

public enum DimensionType {

	Partner(SortType.NATURAL_LABEL),
	Activity(SortType.DEFINED),
	ActivityCategory(SortType.DEFINED),
	Database(SortType.NATURAL_LABEL),
	AdminLevel(SortType.NATURAL_LABEL),
	Date(SortType.NATURAL_VALUE),
	Status(SortType.DEFINED),
	Indicator(SortType.DEFINED),
	IndicatorCategory(SortType.DEFINED),
	AttributeGroup(SortType.NATURAL_VALUE),
    Site(SortType.NATURAL_LABEL), 
    Project(SortType.NATURAL_LABEL), 
    Location(SortType.NATURAL_LABEL),
    Target(SortType.DEFINED);

	private SortType sortOrder;

	DimensionType(SortType sortOrder) {
	this.sortOrder = sortOrder;
	}

	public SortType getSortOrder() {
	return this.sortOrder;
	}
	
}
