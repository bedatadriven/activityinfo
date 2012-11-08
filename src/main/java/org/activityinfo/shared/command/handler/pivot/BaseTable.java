package org.activityinfo.shared.command.handler.pivot;

import java.util.Collections;
import java.util.Map;

import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.handler.pivot.bundler.Bundler;
import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.content.TargetCategory;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;

/**
 * Defines a base table of values to be aggregated
 * and collected by the PivotQuery
 */
public abstract class BaseTable {
	
	/**
	 * 
	 * @param dimensions
	 * @return true if this base table is applicable for the given set of dimensions, 
	 * false if not
	 */
	public abstract boolean accept(PivotSites command);
	
	
	public abstract void setupQuery(PivotSites command, SqlQuery query);

	/**
	 * 
	 * @param type
	 * @return the fully-qualified table and column containing the id for this dimension
	 */
	public abstract String getDimensionIdColumn(DimensionType type);


	public abstract String getDateCompleteColumn();
	
	public abstract TargetCategory getTargetCategory();
	 
	
}
