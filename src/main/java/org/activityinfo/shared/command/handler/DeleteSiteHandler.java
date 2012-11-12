package org.activityinfo.shared.command.handler;

import java.util.Date;

import org.activityinfo.shared.command.DeleteSite;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.db.Tables;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeleteSiteHandler implements CommandHandlerAsync<DeleteSite, VoidResult> {

	@Override
	public void execute(DeleteSite command, ExecutionContext context,
			final AsyncCallback<VoidResult> callback) {

		context.getTransaction().executeSql("DELETE FROM indicatorvalue WHERE ReportingPeriodId IN " +
				"(SELECT ReportingPeriodId FROM reportingperiod WHERE siteid = ?)" ,
				new Object[] { command.getId() });
	
		SqlUpdate.delete(Tables.ATTRIBUTE_VALUE)
				.where("siteId", command.getId());
	
		SqlUpdate.delete(Tables.REPORTING_PERIOD)
				.where("siteId", command.getId())
				.execute(context.getTransaction());
		
		SqlUpdate.update(Tables.SITE)
				.value("dateDeleted", new Date())
				.value("timeEdited", new Date().getTime())
				.where("siteId", command.getId())
				.execute(context.getTransaction(), new SqlResultCallback() {
					
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						callback.onSuccess(null);
					}
				});
		
	}

}
