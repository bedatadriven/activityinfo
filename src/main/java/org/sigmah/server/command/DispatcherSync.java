package org.sigmah.server.command;

import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;

import com.google.inject.ImplementedBy;


@ImplementedBy(DispatcherSyncImpl.class)
public interface DispatcherSync {

	<C extends Command<R>, R extends CommandResult> R execute(C command);

}
