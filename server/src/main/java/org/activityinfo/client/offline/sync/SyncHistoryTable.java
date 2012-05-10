package org.activityinfo.client.offline.sync;

import java.util.Date;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.util.SqlSingleColumnTable;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class SyncHistoryTable extends SqlSingleColumnTable<Date> {

	public SyncHistoryTable(SqlDatabase db) {
		super(db, "sync_history", "lastUpdate");
	}

	public void update() {
		put(new Date(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {				
			}

			@Override
			public void onSuccess(Void result) {				
			}
		});
	}

	@Override
	protected Date convertFromResultSet(SqlResultSet results) {
		double millis = results.getRow(0).getSingleDouble();
		return new Date((long)millis);
	}

	@Override
	protected Object convertToParameter(Date value) {
		return value.getTime();
	}	
}
