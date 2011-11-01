package org.sigmah.server.command;

import org.sigmah.server.endpoint.gwtrpc.handler.HandlerUtil;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class DispatcherSync {
	
	private final Injector injector;
	private final Provider<User> userProvider;

	@Inject
	public DispatcherSync(Injector injector, Provider<User> userProvider) {
		super();
		this.injector = injector;
		this.userProvider = userProvider;
	}
	
	public <C extends Command<R>, R extends CommandResult> R execute(C command) {
		try {
			return (R)HandlerUtil.execute(injector, command, userProvider.get());
		} catch (CommandException e) {
			throw new RuntimeException(e);
		}
	}
}
