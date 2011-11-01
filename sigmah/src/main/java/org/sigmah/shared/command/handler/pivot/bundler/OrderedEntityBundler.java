package org.sigmah.shared.command.handler.pivot.bundler;

import org.sigmah.shared.command.result.Bucket;
import org.sigmah.shared.report.content.EntityCategory;
import org.sigmah.shared.report.model.Dimension;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class OrderedEntityBundler implements Bundler {
    private final Dimension dimension;
    private final String idAlias;
    private final String labelAlias;
    private final String sortOrderAlias;

    public OrderedEntityBundler(Dimension dimension, String id, String label, String sortOrder) {
        this.dimension = dimension;
        this.idAlias = id;
        this.labelAlias = label;
        this.sortOrderAlias = sortOrder;
    }

    @Override
	public void bundle(SqlResultSetRow row, Bucket bucket) {
        bucket.setCategory(dimension, new EntityCategory(
                row.getInt(idAlias),
                row.getString(labelAlias),
                row.getInt(sortOrderAlias)));
    }
}