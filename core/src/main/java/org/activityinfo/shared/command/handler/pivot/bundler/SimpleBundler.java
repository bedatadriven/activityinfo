package org.activityinfo.shared.command.handler.pivot.bundler;

import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.report.content.SimpleCategory;
import org.activityinfo.shared.report.model.Dimension;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class SimpleBundler implements Bundler {
    private final Dimension dimension;
    private final String labelColumnIndex;

    public SimpleBundler(Dimension dimension, String label) {
        this.dimension = dimension;
        this.labelColumnIndex = label;
    }

    @Override
    public void bundle(SqlResultSetRow rs, Bucket bucket) {
        bucket.setCategory(dimension, new SimpleCategory(rs.getString(labelColumnIndex)));
    }
}