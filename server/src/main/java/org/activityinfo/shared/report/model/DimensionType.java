/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.model;

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
