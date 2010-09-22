/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

public enum DimensionType {

	Partner(SortOrder.NATURAL_LABEL),
	Activity(SortOrder.DEFINED),
	ActivityCategory(SortOrder.DEFINED),
	Database(SortOrder.NATURAL_LABEL),
	AdminLevel(SortOrder.NATURAL_LABEL),
	Date(SortOrder.NATURAL_VALUE),
	Status(SortOrder.DEFINED),
	Indicator(SortOrder.DEFINED),
	IndicatorCategory(SortOrder.DEFINED),
	AttributeGroup(SortOrder.NATURAL_VALUE),
    Site(SortOrder.NATURAL_LABEL);

	private SortOrder sortOrder;

	DimensionType(SortOrder sortOrder) {
	this.sortOrder = sortOrder;
	}

	public SortOrder getSortOrder() {
	return this.sortOrder;
	}
	
}
