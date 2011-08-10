package org.sigmah.client.offline.sync;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.util.SqlKeyValueTable;

public class SyncRegionTable extends SqlKeyValueTable {

	public SyncRegionTable(SqlDatabase db) {
		super(db, "sync_regions", "id", "local_version");
	}

}
