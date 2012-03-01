package org.sigmah.client.offline.sync;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.util.SqlKeyValueTable;

/**
 * client-side table that keeps track of the local
 * versions of the various sync regions.
 */
public class SyncRegionTable extends SqlKeyValueTable {

	public SyncRegionTable(SqlDatabase db) {
		super(db, "sync_regions", "id", "localVersion");
	}

}
