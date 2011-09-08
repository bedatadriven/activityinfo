package org.sigmah.shared.dao.pivot.bundler;

import org.sigmah.shared.dao.pivot.Bucket;
import org.sigmah.shared.report.content.EntityCategory;
import org.sigmah.shared.report.model.Dimension;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class EntityBundler implements Bundler {
    private final Dimension dimension;
    private final String idAlias;
    private final String labelAlias;

    public EntityBundler(Dimension key, String idAlias, String labelAlias) {
        this.dimension = key;
        this.idAlias = idAlias;
        this.labelAlias = labelAlias;
    }

	@Override
    public void bundle(SqlResultSetRow row, Bucket bucket)  {
        bucket.setCategory(dimension, new EntityCategory(
                row.getInt(idAlias),
                row.getString(labelAlias)));
    }
}