package org.activityinfo.shared.command.handler.pivot.bundler;

import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.report.content.YearCategory;
import org.activityinfo.shared.report.model.Dimension;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class YearBundler implements Bundler {
    private final Dimension dimension;
    private final String yearAlias;

    public YearBundler(Dimension dimension, String yearAlias) {
        this.dimension = dimension;
        this.yearAlias = yearAlias;
    }

    @Override
    public void bundle(SqlResultSetRow rs, Bucket bucket)  {
    	// the year can be null in cases where a site does not yet have a reporting period
    	// and we query for site counts
    	if(!rs.isNull(yearAlias)) {
	        bucket.setCategory(dimension, new YearCategory(
	                rs.getInt(yearAlias)));
    	}
    }
}