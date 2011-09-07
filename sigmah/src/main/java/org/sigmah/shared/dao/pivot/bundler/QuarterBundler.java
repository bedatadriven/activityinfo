package org.sigmah.shared.dao.pivot.bundler;

import org.sigmah.shared.dao.pivot.Bucket;
import org.sigmah.shared.report.content.QuarterCategory;
import org.sigmah.shared.report.model.Dimension;

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