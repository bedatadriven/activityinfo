package org.activityinfo.shared.command.handler.pivot.bundler;

import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.report.content.MonthCategory;
import org.activityinfo.shared.report.model.Dimension;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class MonthBundler implements Bundler {
    private final Dimension dimension;
    private final String yearAlias;
    private final String monthAlias;


    public MonthBundler(Dimension dimension, String yearAlias, String monthAlias) {
        this.dimension = dimension;
        this.yearAlias = yearAlias;
        this.monthAlias = monthAlias;
	}

	@Override
    public void bundle(SqlResultSetRow rs, Bucket bucket)  {
        
    	// the year can be null in cases where a site does not yet have a reporting period
		if(!rs.isNull(yearAlias)) {
			int year = rs.getInt(yearAlias);
	        int month = rs.getInt(monthAlias);
	
	        bucket.setCategory(dimension, new MonthCategory(year, month));
		}
    }
}