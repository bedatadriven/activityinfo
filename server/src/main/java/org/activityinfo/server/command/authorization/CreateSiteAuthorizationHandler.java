package org.activityinfo.server.command.authorization;

import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.exception.NotAuthorizedException;
import org.activityinfo.shared.command.handler.AuthorizationHandler;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.apache.log4j.Logger;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CreateSiteAuthorizationHandler implements AuthorizationHandler<CreateSite> {

	private static final Logger LOGGER = Logger.getLogger(CreateSiteAuthorizationHandler.class);
	
	@Override
	public void authorize(final CreateSite command, 
			final ExecutionContext context,
			final AsyncCallback<Void> callback) {

		SqlQuery.select(
				"db.ownerUserId",
				"p.allowEditAll",
				"p.allowEdit",
				"p.partnerId")
			.from("Activity", "a")
			.leftJoin("UserDatabase", "db").on("a.DatabaseId=db.DatabaseId")
			.leftJoin(	
					SqlQuery.selectAll().from("UserPermission")
					.where("UserPermission.UserId")
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
							LOGGER.error("CreateSite Authorization failed: command = " + command + 
									", user = " + context.getUser().getId());
							callback.onFailure(new NotAuthorizedException());
						}
						
					}
				}
			});		
	}
}
