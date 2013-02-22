package org.activityinfo.server.command.authorization;

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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.exception.NotAuthorizedException;
import org.activityinfo.shared.command.handler.AuthorizationHandler;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.db.Tables;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CreateSiteAuthorizationHandler implements AuthorizationHandler<CreateSite> {

	private static final Logger LOGGER = Logger.getLogger(CreateSiteAuthorizationHandler.class.getName());
	
	@Override
	public void authorize(final CreateSite command, 
			final ExecutionContext context,
			final AsyncCallback<Void> callback) {

		SqlQuery.select(
				"db.ownerUserId",
				"p.allowEditAll",
				"p.allowEdit",
				"p.partnerId")
			.from(Tables.ACTIVITY, "a")
			.leftJoin(Tables.USER_DATABASE, "db").on("a.DatabaseId=db.DatabaseId")
			.leftJoin(	
					SqlQuery.selectAll()
					.from(Tables.USER_PERMISSION, "perm")
					.where("perm.UserId")
					.equalTo(context.getUser().getId()), "p")
					.on("db.DatabaseId=p.DatabaseId")
			.where("a.ActivityId").equalTo(command.getActivityId())
			.execute(context.getTransaction(), new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					if(results.getRows().isEmpty()) {
						callback.onFailure(new RuntimeException("No such activity with id " + command.getActivityId()));
					} else {
						SqlResultSetRow row = results.getRow(0);
						if(row.getInt("ownerUserId") == context.getUser().getId()) {
							callback.onSuccess(null);
						} else if(!row.isNull("allowEditAll") && row.getBoolean("allowEditAll")) {
							callback.onSuccess(null);
						} else if(!row.isNull("allowEdit") && row.getBoolean("allowEdit") &&
								row.getInt("partnerId") == command.getPartnerId()) {
							callback.onSuccess(null);
						} else {
							LOGGER.log(Level.SEVERE, "CreateSite Authorization failed: command = " + command + 
									", user = " + context.getUser().getId());
							callback.onFailure(new NotAuthorizedException());
						}
						
					}
				}
			});		
	}
}
