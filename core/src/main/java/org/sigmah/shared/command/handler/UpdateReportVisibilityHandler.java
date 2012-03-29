package org.sigmah.shared.command.handler;

import org.sigmah.shared.command.UpdateReportVisibility;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ReportVisibilityDTO;

import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UpdateReportVisibilityHandler implements CommandHandlerAsync<UpdateReportVisibility, VoidResult>{

	@Override
	public void execute(UpdateReportVisibility command,
			ExecutionContext context, AsyncCallback<VoidResult> callback) {

		SqlUpdate.delete("reportvisibility").where("reportid", command.getReportId()).execute(context.getTransaction());
		for(ReportVisibilityDTO dto : command.getList()) {
			SqlUpdate.delete("reportvisibility")
			.where("reportid", command.getReportId())
			.where("databaseid", dto.getDatabaseId())
			.execute(context.getTransaction());
			
			if(dto.isVisible()) {
				SqlInsert.insertInto("reportvisibility")
					.value("reportid", command.getReportId())
					.value("databaseid", dto.getDatabaseId())
					.value("defaultDashboard", dto.isDefaultDashboard())
					.execute(context.getTransaction());
			} 
		}
		callback.onSuccess(null);
		
	}
}
