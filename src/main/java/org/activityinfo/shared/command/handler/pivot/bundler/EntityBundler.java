package org.activityinfo.shared.command.handler.pivot.bundler;

import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.model.Dimension;

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
		if(!row.isNull(idAlias)) {
	        bucket.setCategory(dimension, new EntityCategory(
	                row.getInt(idAlias),
	                row.getString(labelAlias)));
		}
    }
	
	
}