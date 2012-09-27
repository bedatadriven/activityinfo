package org.activityinfo.shared.command.handler;

import org.activityinfo.shared.command.DeleteReport;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.db.Tables;

import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeleteReportHandler implements CommandHandlerAsync<DeleteReport, VoidResult>{

	@Override
	public void execute(DeleteReport command, ExecutionContext context,
			AsyncCallback<VoidResult> callback) {
		SqlUpdate.delete(Tables.REPORT_TEMPLATE)
			.where("reporttemplateid", command.getReportId())
			.execute(context.getTransaction());
		SqlUpdate.delete(Tables.REPORT_SUBSCRIPTION)
			.where("reportId", command.getReportId())
			.execute(context.getTransaction());
		SqlUpdate.delete(Tables.REPORT_VISIBILITY)
			.where("reportId", command.getReportId())
			.execute(context.getTransaction());

		callback.onSuccess(null);
	}

}
