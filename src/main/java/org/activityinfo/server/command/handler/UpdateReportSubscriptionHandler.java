package org.activityinfo.server.command.handler;

import org.activityinfo.shared.command.UpdateReportSubscription;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.db.Tables;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UpdateReportSubscriptionHandler implements CommandHandlerAsync<UpdateReportSubscription, VoidResult>{

	@Override
	public void execute(final UpdateReportSubscription command,
			final ExecutionContext context, final AsyncCallback<VoidResult> callback) {
	
		SqlUpdate update = SqlUpdate.update(Tables.REPORT_SUBSCRIPTION)
			.valueIfNotNull("dashboard", command.getPinnedToDashboard())
			.valueIfNotNull("emailday", command.getEmailDay())
			.where("reportId", command.getReportId())
			.where("userId", context.getUser().getId());
		
		if(command.getEmailDelivery() != null) {
			update.value("emaildelivery",  command.getEmailDelivery().name());
		}
		
		if(update.isEmpty()) {
			callback.onSuccess(null);
		} else {
			update.execute(context.getTransaction(), new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					if(results.getRowsAffected() != 0) {
						// successfully updated
						callback.onSuccess(null);
					} else {
						// need to insert new record
						
						SqlInsert.insertInto(Tables.REPORT_SUBSCRIPTION)
						.value("dashboard", command.getPinnedToDashboard() != null && command.getPinnedToDashboard())
						.value("subscribed", false)
						.value("userId", context.getUser().getId())
						.value("reportId", command.getReportId())
						.execute(context.getTransaction(), new AsyncCallback<Integer>() {
							
							@Override
							public void onSuccess(Integer result) {
								callback.onSuccess(null);
							}
							
							@Override
							public void onFailure(Throwable caught) {
								callback.onFailure(caught);
							}
						});
						
					}
					
				}
			});	
		}
	}

	
}
