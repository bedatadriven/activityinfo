package org.sigmah.server.command;

import org.sigmah.server.command.handler.HandlerUtil;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.auth.AuthenticatedUser;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class DispatcherSyncImpl implements DispatcherSync {

	private final Injector injector;
	private final Provider<AuthenticatedUser> userProvider;

	@Inject
	public DispatcherSyncImpl(Injector injector, Provider<AuthenticatedUser> userProvider) {
		this.injector = injector;
		this.userProvider = userProvider;
	}
	
	public <C extends Command<R>, R extends CommandResult> R execute(C command) {
		try {
			User user = new User();
			user.setId(userProvider.get().getUserId());
			user.setEmail(userProvider.get().getEmail());
			user.setLocale(userProvider.get().getUserLocale());
			
			return (R)HandlerUtil.execute(injector, command, user);
		} catch (CommandException e) {
			throw new RuntimeException(e);
		}
	}
}
