/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.remote;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.sigmah.client.dispatch.*;
import org.sigmah.client.offline.OfflineStatus;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.OfflineSupport;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.handler.GetSchemaHandler;
import org.sigmah.shared.command.handler.GetSitesHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;


public class SwitchingDispatcher implements Dispatcher, DispatchEventSource {

	private RemoteDispatcher remoteDispatcher;
    protected final OfflineStatus status;
    protected final Authentication authentication;
    private final Provider<GetSchemaHandler> getSchemaHandler;
    private final Provider<GetSitesHandler> getSitesHandler;

    @Inject
	public SwitchingDispatcher(RemoteDispatcher remoteDispatcher, OfflineStatus status, Authentication authentication,
                            Provider<GetSchemaHandler> getSchemaHandler, Provider<GetSitesHandler> getSitesHandler) {
		this.remoteDispatcher = remoteDispatcher;
		this.status = status;
		this.authentication = authentication;
        this.getSchemaHandler = getSchemaHandler;
        this.getSitesHandler = getSitesHandler;
    }
	
	@Override
	public <T extends CommandResult> void execute(Command<T> command,
			AsyncMonitor monitor, AsyncCallback<T> callback) {
		// TODO ; turn this back on when stable
		if (status.isOfflineEnabled() && command instanceof OfflineSupport) {
			Log.debug("Running command on local:" + command.toString());
			runCommandOnLocal(command, monitor, callback);
		} else {
			remoteDispatcher.execute(command, monitor, callback);
		}
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
			return (T) getSchemaHandler.get();
		} else if (c instanceof GetSites) {
			return (T) getSitesHandler.get();
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
