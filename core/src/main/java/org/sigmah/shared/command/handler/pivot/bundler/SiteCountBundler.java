package org.sigmah.shared.command.handler.pivot.bundler;

import org.sigmah.shared.command.result.Bucket;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class SiteCountBundler implements Bundler {
    private String countAlias;
	
	public SiteCountBundler(String count) {
		this.countAlias = count;
	}

	@Override
	public void bundle(SqlResultSetRow rs, Bucket bucket) {
        bucket.setDoubleValue((double) rs.getInt(countAlias));
    }
}