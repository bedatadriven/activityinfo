package org.sigmah.shared.dao.pivot.bundler;

import org.sigmah.shared.dao.pivot.Bucket;
import org.sigmah.shared.report.content.YearCategory;
import org.sigmah.shared.report.model.Dimension;

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
        bucket.setCategory(dimension, new YearCategory(
                rs.getInt(yearAlias)));
    }
}