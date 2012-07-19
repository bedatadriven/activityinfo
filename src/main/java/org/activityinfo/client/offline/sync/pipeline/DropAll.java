package org.activityinfo.client.offline.sync.pipeline;

import org.activityinfo.client.offline.command.CommandQueue;
import org.activityinfo.client.offline.sync.SyncHistoryTable;
import org.activityinfo.client.offline.sync.SyncRegionTable;

import com.bedatadriven.rebar.async.AsyncCommand;
import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
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
		conn.transaction(new SqlTransactionCallback() {
			
			@Override
			public void begin(SqlTransaction tx) {
				conn.dropAllTables(tx);
				new SyncRegionTable(conn).createTableIfNotExists(tx);
				new SyncHistoryTable(conn).createTableIfNotExists(tx);
				CommandQueue.createTableIfNotExists(tx);
			}

			@Override
			public void onError(SqlException e) {
				callback.onFailure(e);
			}

			@Override
			public void onSuccess() {
				callback.onSuccess(null);
			}
		});
	}
}
