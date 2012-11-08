package org.activityinfo.shared.command.handler.pivot.bundler;

import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.report.content.TargetCategory;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class TargetBundler implements Bundler {

	public static final String TARGET_ALIAS = "_TARGET";

	@Override
	public void bundle(SqlResultSetRow row, Bucket bucket) {
		if(row.getInt(TARGET_ALIAS) == 0) {
			bucket.setCategory(new Dimension(DimensionType.Target), TargetCategory.REALIZED);
		} else {
			bucket.setCategory(new Dimension(DimensionType.Target), TargetCategory.TARGETED);
		}
	}
	
	
	
}
