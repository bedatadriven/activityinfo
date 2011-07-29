package org.sigmah.server.endpoint.gwtrpc.handler;

import org.sigmah.shared.command.GetLocationsWithoutGpsCoordinates;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;

public class GetLocationsWithoutGpsCoordinatesHandler implements CommandHandler<GetLocationsWithoutGpsCoordinates> {

	@Override
	public CommandResult execute(GetLocationsWithoutGpsCoordinates cmd, User user)
			throws CommandException {
		// TODO Auto-generated method stub
		return null;
	}

}
