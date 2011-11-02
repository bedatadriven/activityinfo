package org.sigmah.server.command.handler;

import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.Ping;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.exception.CommandException;

public class PingHandler implements CommandHandler<Ping> {

	@Override
	public CommandResult execute(Ping cmd, User user) throws CommandException {
		return new VoidResult();
	}

}
