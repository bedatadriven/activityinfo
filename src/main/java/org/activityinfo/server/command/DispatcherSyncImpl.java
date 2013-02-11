package org.activityinfo.server.command;

import org.activityinfo.login.shared.AuthenticatedUser;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.endpoint.gwtrpc.RemoteExecutionContext;
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
	
	@Override
	public <C extends Command<R>, R extends CommandResult> R execute(C command) throws CommandException {
		if(RemoteExecutionContext.inProgress()) {
			return RemoteExecutionContext.current().execute(command);
		} else {
			User user = new User();
			user.setId(userProvider.get().getUserId());
			user.setEmail(userProvider.get().getEmail());
			user.setLocale(userProvider.get().getUserLocale());
			
			RemoteExecutionContext context = new RemoteExecutionContext(injector);
			return context.startExecute(command);
		}
	}
}
