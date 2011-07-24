package org.sigmah.client.offline.command.handler;

import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.handler.CommandHandler;

public interface PartialCommandHandler<T extends Command> extends CommandHandler<T> {

	boolean canExecute(T c);
	
}
