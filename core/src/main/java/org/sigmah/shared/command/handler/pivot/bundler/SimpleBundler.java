package org.sigmah.shared.command.handler.pivot.bundler;

import org.sigmah.shared.command.result.Bucket;
import org.sigmah.shared.report.content.SimpleCategory;
import org.sigmah.shared.report.model.Dimension;

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