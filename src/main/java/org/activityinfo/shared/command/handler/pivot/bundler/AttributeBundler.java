package org.activityinfo.shared.command.handler.pivot.bundler;

import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.report.content.AttributeCategory;
import org.activityinfo.shared.report.model.Dimension;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class AttributeBundler implements Bundler {
    private final Dimension dimension;
    private final String valueColumnAlias;
    private final String orderColumnAlias;
    
    public AttributeBundler(Dimension dimension, String valueColumnAlias,
        String orderColumnAlias) {
        super();
        this.dimension = dimension;
        this.valueColumnAlias = valueColumnAlias;
        this.orderColumnAlias = orderColumnAlias;
    }

    @Override
    public void bundle(SqlResultSetRow row, Bucket bucket) {
        bucket.setCategory(dimension,
            new AttributeCategory(row.getString(valueColumnAlias), row.getInt(orderColumnAlias)));
    }

}
