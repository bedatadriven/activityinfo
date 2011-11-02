package org.sigmah.server.command.handler;

import org.sigmah.shared.command.GetDashboard;
import org.sigmah.shared.command.handler.CommandHandlerAsync;
import org.sigmah.shared.command.handler.ExecutionContext;
import org.sigmah.shared.command.result.DashboardSettingsResult;
import org.sigmah.shared.dto.DashboardSettingsDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetDashboardHandler implements CommandHandlerAsync<GetDashboard, DashboardSettingsResult> {
	@Override
	public void execute(
			GetDashboard command, 
			ExecutionContext context,
			AsyncCallback<DashboardSettingsResult> callback) {
		
		callback.onSuccess(new DashboardSettingsResult(DashboardSettingsDTO.createDefault())); 
	}
}
