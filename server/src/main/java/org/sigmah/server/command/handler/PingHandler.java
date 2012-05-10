package org.sigmah.server.command.handler;

import org.activityinfo.shared.command.Ping;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.exception.CommandException;
import org.sigmah.server.database.hibernate.entity.User;

public class PingHandler implements CommandHandler<Ping> {

	@Override
	public CommandResult execute(Ping cmd, User user) throws CommandException {
		return new VoidResult();
	}

}
