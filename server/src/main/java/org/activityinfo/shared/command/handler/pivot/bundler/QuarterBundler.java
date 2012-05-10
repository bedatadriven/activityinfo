package org.activityinfo.shared.command.handler.pivot.bundler;

import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.report.content.QuarterCategory;
import org.activityinfo.shared.report.model.Dimension;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class QuarterBundler implements Bundler {
    private final Dimension dimension;
    private final String yearAlias;
    private final String quarterAlias;


    public QuarterBundler(Dimension dimension, String yearAlias,
			String quarterAlias) {
    	this.dimension = dimension;
    	this.yearAlias = yearAlias;
    	this.quarterAlias = quarterAlias;
	}

	@Override
    public void bundle(SqlResultSetRow rs, Bucket bucket) {
        int year = rs.getInt(yearAlias);
        int quarter = rs.getInt(quarterAlias);

        bucket.setCategory(dimension, new QuarterCategory(year, quarter));
    }
}