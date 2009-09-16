package org.activityinfo.server.report.renderer.html;

import org.activityinfo.shared.report.model.DimensionType;

public class CssStyles {

	public static final String REPORT_TITLE = "r-title";

	public static final String REPORT_CONTAINER = "r-report";
	
	public static final String ELEMENT_CONTAINER = "r-element";
	
	public static final String FILTER_DESCRIPTION = "r-filter-desc";
	
	public static String DIMENSION(DimensionType type) {
		return "r-dimension-" + type.toString().toLowerCase();
	}
	
	public static final String PIVOT_TABLE = "r-pivot";

	public static final String NO_DATA = "r-nodata";

	
	public static final String VALUE_CELL = "r-value";
	public static final String EMPTY_VALUE_CELL = "r-empty-value";
	
	public static final String ROW_GROUP = "r-row-group";
	
	public static String INDENT(int i) {
		return "r-indent" + i;
	}
	
	
	
}
