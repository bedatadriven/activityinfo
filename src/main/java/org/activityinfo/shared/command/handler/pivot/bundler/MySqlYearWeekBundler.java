package org.activityinfo.shared.command.handler.pivot.bundler;

import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.report.content.WeekCategory;
import org.activityinfo.shared.report.model.Dimension;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class MySqlYearWeekBundler implements Bundler {

	private Dimension dimension;
	private String weekAlias;

	public MySqlYearWeekBundler(Dimension dimension, String weekAlias) {
		this.dimension = dimension;
		this.weekAlias = weekAlias;
	}

	@Override
	public void bundle(SqlResultSetRow row, Bucket bucket) {
		if(row.isNull(weekAlias)) {
			String yearWeek = row.getString(weekAlias);
			if(yearWeek.length() == 6) {
				bucket.setCategory(dimension, new WeekCategory(
						Integer.parseInt(yearWeek.substring(0, 4)),
						Integer.parseInt(yearWeek.substring(4, 6))));
			}
		}
	}

}
