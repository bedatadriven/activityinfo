package org.sigmah.client.dispatch.remote;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.CommandProxy;
import org.sigmah.client.dispatch.DispatchEventSource;
import org.sigmah.client.dispatch.DispatchListener;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.inject.AppInjector;
import org.sigmah.client.offline.OfflineStatus;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dao.UserDAO;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;


public class SwitchingDispatcher implements Dispatcher, DispatchEventSource {

	private RemoteDispatcher remoteDispatcher;
    protected final OfflineStatus status;
    protected final Authentication authentication;
    private UserDAO userDAO; 
    private AppInjector injector;
    
	@Inject
	public SwitchingDispatcher(AppInjector injector, RemoteDispatcher remoteDispatcher, OfflineStatus status, Authentication authentication, UserDAO userDAO) {
		this.remoteDispatcher = remoteDispatcher;
		this.status = status;
		this.authentication = authentication;
		this.injector = injector;
		this.userDAO = userDAO;
	}
	
	@Override
	public <T extends CommandResult> void execute(Command<T> command,
			AsyncMonitor monitor, AsyncCallback<T> callback) {
		// TODO ; turn this back on when stable
	//	if (status.isOfflineEnabled() && command instanceof OfflineSupport) {
	//		runCommandOnLocal(command, monitor, callback);
	//		Log.debug("Running command on local:" + command.toString());
	//	} else {
			remoteDispatcher.execute(command, monitor, callback);
	//	}
	}

	@SuppressWarnings("unchecked")
	private <T extends CommandResult> void runCommandOnLocal(Command<?> command, AsyncMonitor montior, AsyncCallback<T> callback) {
		int userId = authentication.getUserId();
		// pass a dummy user to the command
		User user =  new User();
		user.setId(userId);
		//	this.userDAO.findById();
		CommandHandler<Command<?>> handler = getHandler(command);
		try {
			CommandResult result = handler.execute(command, user);
			Log.debug("Command success");
			callback.onSuccess((T)result);
		} catch (CommandException e) {
			Log.debug("Command failure");
			callback.onFailure(e);
		}
	}
	
	private <T extends CommandHandler> T getHandler(Command c) {
		// TODO enable more offline schema handlers
		if (c instanceof GetSchema) {
			return (T) injector.createGetSchemaHandler();
		} else if (c instanceof GetSites) {
			return (T) injector.createGetSitesHandler();
		} else {				
			throw new IllegalArgumentException("No handler class for " + c);
		}
	}
	
	
	@Override
	public <T extends Command> void registerListener(Class<T> commandClass,
			DispatchListener<T> listener) {
		// TODO Set up cache etc
		Log.debug("*** register listener");
		
	}

	@Override
	public <T extends Command> void registerProxy(Class<T> commandClass,
			CommandProxy<T> proxy) {
		// TODO Auto-generated method stub
		Log.debug("*** register proxy");
	
	}
}
