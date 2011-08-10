package org.sigmah.client.offline.sync;

import java.util.Date;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.util.SqlSingleColumnTable;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class SyncHistoryTable extends SqlSingleColumnTable<Double> {

	public SyncHistoryTable(SqlDatabase db) {
		super(db, "sync_history", "lastUpdate");
	}

	public void update() {
		put((double)new Date().getTime(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {				
			}

			@Override
			public void onSuccess(Void result) {				
			}
		});
	}
	
}
