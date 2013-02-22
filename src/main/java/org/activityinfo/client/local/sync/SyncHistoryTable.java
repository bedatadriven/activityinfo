package org.activityinfo.client.local.sync;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Date;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.util.SqlSingleColumnTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;


public class SyncHistoryTable extends SqlSingleColumnTable<Date> {

	@Inject
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
