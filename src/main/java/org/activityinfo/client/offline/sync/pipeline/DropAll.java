package org.activityinfo.client.offline.sync.pipeline;

import org.activityinfo.client.offline.command.CommandQueue;
import org.activityinfo.client.offline.sync.SyncHistoryTable;
import org.activityinfo.client.offline.sync.SyncRegionTable;

import com.bedatadriven.rebar.async.AsyncCommand;
import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.fn.AsyncSql;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class DropAll implements AsyncCommand {

	private final SqlDatabase conn;

	@Inject
	public DropAll(SqlDatabase conn) {
		super();
		this.conn = conn;
	}

	@Override
	public void execute(final AsyncCallback<Void> callback) {
		
		conn.execute(AsyncSql.sequence(
				AsyncSql.dropAllTables(),
				new SyncRegionTable(conn).createTableIfNotExists(),
				new SyncHistoryTable(conn).createTableIfNotExists(),
				CommandQueue.createTableIfNotExists()),
				callback);		
	}
}
