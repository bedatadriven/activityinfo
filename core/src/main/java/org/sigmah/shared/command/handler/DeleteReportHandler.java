package org.sigmah.shared.command.handler;

import org.sigmah.shared.command.DeleteReport;
import org.sigmah.shared.command.result.VoidResult;

import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeleteReportHandler implements CommandHandlerAsync<DeleteReport, VoidResult>{

	@Override
	public void execute(DeleteReport command, ExecutionContext context,
			AsyncCallback<VoidResult> callback) {
		SqlUpdate.delete("reporttemplate")
			.where("reporttemplateid", command.getReportId())
			.execute(context.getTransaction());
		SqlUpdate.delete("reportsubscription")
			.where("reportId", command.getReportId())
			.execute(context.getTransaction());
		SqlUpdate.delete("reportvisibility")
			.where("reportId", command.getReportId())
			.execute(context.getTransaction());

		callback.onSuccess(null);
	}

}
