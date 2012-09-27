package org.activityinfo.shared.command.handler;

import org.activityinfo.shared.command.UpdateReportVisibility;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.ReportVisibilityDTO;

import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UpdateReportVisibilityHandler implements CommandHandlerAsync<UpdateReportVisibility, VoidResult>{

	@Override
	public void execute(UpdateReportVisibility command,
			ExecutionContext context, AsyncCallback<VoidResult> callback) {

		SqlUpdate.delete(Tables.REPORT_VISIBILITY)
			.where("reportid", command.getReportId())
			.execute(context.getTransaction());
		for(ReportVisibilityDTO dto : command.getList()) {
			SqlUpdate.delete(Tables.REPORT_VISIBILITY)
			.where("reportid", command.getReportId())
			.where("databaseid", dto.getDatabaseId())
			.execute(context.getTransaction());
			
			if(dto.isVisible()) {
				SqlInsert.insertInto(Tables.REPORT_VISIBILITY)
					.value("reportid", command.getReportId())
					.value("databaseid", dto.getDatabaseId())
					.value("defaultDashboard", dto.isDefaultDashboard())
					.execute(context.getTransaction());
			} 
		}
		callback.onSuccess(null);
		
	}
}
