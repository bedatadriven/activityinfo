package org.activityinfo.server.command;

import org.activityinfo.server.command.handler.HandlerUtil;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;

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
