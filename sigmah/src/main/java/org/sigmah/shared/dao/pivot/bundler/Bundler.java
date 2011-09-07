package org.sigmah.shared.dao.pivot.bundler;

import org.sigmah.shared.dao.pivot.Bucket;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;


/**
 * Internal interface to a group of objects that are responsible for
 * bundling results from the SQL ResultSet object into a Bucket
 */
public interface Bundler {
    public void bundle(SqlResultSetRow row, Bucket bucket);
}