package org.sigmah.server.endpoint.gwtrpc.handler;

import org.sigmah.shared.command.GetDashboard;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.DashboardSettingsResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.DashboardSettingsDTO;
import org.sigmah.shared.exception.CommandException;

public class GetDashboardHandler implements CommandHandler<GetDashboard> {

	@Override
	public CommandResult execute(GetDashboard cmd, User user)
			throws CommandException {
		DashboardSettingsDTO settings = new DashboardSettingsDTO();
		settings.setAmountColumns(3);
		return new DashboardSettingsResult(settings);
	}

}
