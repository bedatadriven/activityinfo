/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

/**
 * Permits offline access to ActivityInfo with the help of Gears
 */
package org.activityinfo.client.local;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.SessionUtil;
import org.activityinfo.client.authentication.ClientSideAuthProvider;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.remote.Remote;
import org.activityinfo.client.i18n.UIConstants;
import org.activityinfo.client.local.LocalStateChangeEvent.State;
import org.activityinfo.client.local.capability.LocalCapabilityProfile;
import org.activityinfo.client.local.capability.PermissionRefusedException;
import org.activityinfo.client.local.sync.AppOutOfDateException;
import org.activityinfo.client.local.sync.SyncCompleteEvent;
import org.activityinfo.client.local.sync.SyncHistoryTable;
import org.activityinfo.client.local.sync.SyncStatusEvent;
import org.activityinfo.client.local.sync.Synchronizer;
import org.activityinfo.client.local.sync.SynchronizerConnectionException;
import org.activityinfo.login.shared.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.InvalidAuthTokenException;

import org.activityinfo.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * This class keeps as much of the offline functionality behind a runAsync
 * clause to defer downloading the related JavaScript until the user actually
 * goes into offline mode.
 */
@Singleton
public class LocalController implements Dispatcher {

	public interface PromptConnectCallback {
		void onCancel();

		void onTryToConnect();
	}

	private final EventBus eventBus;
	private final Provider<Synchronizer> synchronizerProvider;
	private UIConstants uiConstants;
	private final Dispatcher remoteDispatcher;
	private final LocalCapabilityProfile capabilityProfile;
	private final Provider<SyncHistoryTable> historyTable;
	
	private Strategy activeStrategy;
	private Date lastSynced = null;

	@Inject
	public LocalController(EventBus eventBus,
			@Remote Dispatcher remoteDispatcher,
			Provider<Synchronizer> gateway,
			LocalCapabilityProfile capabilityProfile, 
			UIConstants uiConstants,
			Provider<SyncHistoryTable> historyTable) {
		this.eventBus = eventBus;
		this.remoteDispatcher = remoteDispatcher;
		this.synchronizerProvider = gateway;
		this.capabilityProfile = capabilityProfile;
		this.uiConstants = uiConstants;
		this.historyTable = historyTable;
		
		Log.trace("OfflineManager: starting");

		activateStrategy(new LoadingLocalStrategy());
	}
	
	public Date getLastSyncTime() {
		return lastSynced;
	}

	public void install() {
		if (activeStrategy instanceof NotInstalledStrategy) {
			((NotInstalledStrategy) activeStrategy).enableOffline();
		}
	}
	
	public void synchronize() {
		if(activeStrategy instanceof LocalStrategy) {
			((LocalStrategy)activeStrategy).synchronize();
		}
	}

	public State getState() {
		return activeStrategy.getState();
	}
	
	@Override
	public <T extends CommandResult> void execute(Command<T> command,
			AsyncMonitor monitor, AsyncCallback<T> callback) {

		activeStrategy.dispatch(command, callback);
	}

	@Override
	public <T extends CommandResult> void execute(Command<T> command,
			AsyncCallback<T> callback) {
		execute(command, null, callback);
	}

	private void activateStrategy(Strategy strategy) {
		try {
			this.activeStrategy = strategy;
			this.activeStrategy.activate();
			eventBus.fireEvent(new LocalStateChangeEvent(this.activeStrategy.getState()));

		} catch (Exception caught) {
			// errors really ought to be handled by the strategy that is passing
			// control to us
			// but we can't afford to let an uncaught exception go as it could
			// leave the app
			// in a state of limbo
			Log.error("Uncaught exception while activatign strategy, defaulting to Not INstalled");
			activateStrategy(new NotInstalledStrategy());
		}
	}

	private void loadSynchronizerImpl(final AsyncCallback<Synchronizer> callback) {
		Log.trace("loadSynchronizerImpl() starting...");
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable throwable) {
				Log.trace("loadSynchronizerImpl() failed");
				callback.onFailure(throwable);
			}

			@Override
			public void onSuccess() {
				Log.trace("loadSynchronizerImpl() succeeded");

				Synchronizer impl = null;
				try {
					impl = synchronizerProvider.get();
				} catch (Exception caught) {
					Log.error(
							"SynchronizationImpl constructor threw exception",
							caught);
					callback.onFailure(caught);
					return;
				}
				callback.onSuccess(impl);
			}
		});
	}

	private void reportFailure(Throwable throwable) {
		Log.error("Exception in offline controller", throwable);
		
		if (throwable instanceof InvalidAuthTokenException) {
			SessionUtil.forceLogin();

		} else if (throwable instanceof AppOutOfDateException
				|| throwable instanceof IncompatibleRemoteServiceException) {
			// view.promptToReloadForNewVersion();

		} else if (throwable instanceof SynchronizerConnectionException) {
			// view.showSynchronizerConnectionProblem();

		} else {
			// view.showError(throwable.getMessage());
		}
	}

	private abstract class Strategy {
		Strategy activate() {
			return this;
		}

		void dispatch(Command command, AsyncCallback callback) {
			// by default, we send to the server
			remoteDispatcher.execute(command, callback);
		}

		abstract State getState();
	}

	/**
	 * Strategy for handling the state in which offline mode is not at all
	 * available.
	 * 
	 * The only thing the user can do from here is start installation.
	 */
	private class NotInstalledStrategy extends Strategy {

		@Override
		public NotInstalledStrategy activate() {
			return this;
		}

		@Override
		State getState() {
			return State.UNINSTALLED;
		}

		public void enableOffline() {
			Log.trace("enablingOffline() started");
			capabilityProfile.acquirePermission(new AsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					activateStrategy(new InstallingStrategy());
				}

				@Override
				public void onFailure(Throwable caught) {
					if (!(caught instanceof PermissionRefusedException)) {
						reportFailure(caught);
					}
				}
			});
		}
	}

	/**
	 * Strategy for handling the state in which installation is in progress.
	 * Commands continue to be handled by the remote dispatcher during
	 * installation
	 */
	private class InstallingStrategy extends Strategy {

		@Override
		State getState() {
			return State.INSTALLING;
		}

		@Override
		Strategy activate() {
			eventBus.fireEvent(new SyncStatusEvent(uiConstants.starting(), 0));

			loadSynchronizerImpl(new AsyncCallback<Synchronizer>() {
				@Override
				public void onFailure(Throwable caught) {
					activateStrategy(new NotInstalledStrategy());
					reportFailure(caught);
				}

				@Override
				public void onSuccess(final Synchronizer gateway) {
					gateway.install(new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							activateStrategy(new NotInstalledStrategy());
							LocalController.this.reportFailure(caught);
						}

						@Override
						public void onSuccess(Void result) {
							activateStrategy(new LocalStrategy(gateway));
						}
					});
				}
			});
			return this;
		}
	}

	/**
	 * This is a sort of purgatory state that occurs immediately after
	 * while we're determining whether offline mode has been enabled
	 * and then if so, while we'ere loading the offline module 
	 * async fragment.
	 */
	private class LoadingLocalStrategy extends Strategy {

		/**
		 * Commands cannot be executed until everything is loaded...
		 */
		private List<CommandRequest> pending;

		@Override
		State getState() {
			return State.CHECKING;
		}

		@Override
		Strategy activate() {
			pending = new ArrayList<CommandRequest>();
			try {
				historyTable.get().get(new AsyncCallback<Date>() {
					
					@Override
					public void onSuccess(Date result) {
						loadModule();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						abandonShip();
					}
				});
			} catch(Exception e) {
				abandonShip();
			}
			return this;
		}

		private void loadModule() {
			loadSynchronizerImpl(new AsyncCallback<Synchronizer>() {
				@Override
				public void onFailure(Throwable caught) {
					abandonShip(caught);
				}

				@Override
				public void onSuccess(final Synchronizer gateway) {

					gateway.validateOfflineInstalled(new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							abandonShip(caught);
						}

						@Override
						public void onSuccess(Void result) {
							activateStrategy(new LocalStrategy(gateway));
							doDispatch(pending);
						}
					});
				}
			});
		}

		@Override
		void dispatch(Command command, AsyncCallback callback) {
			pending.add(new CommandRequest(command, callback));
		}

		void abandonShip(Throwable caught) {
			reportFailure(caught);
			abandonShip();
		}

		// something went wrong while loading the async fragment or
		// in the boot up, revert to the uninstalled state. the user
		// can always reinstall. (not ideal, obviously)
		void abandonShip() {
			activateStrategy(new NotInstalledStrategy());
			doDispatch(pending);
		}
	}

	/**
	 * Strategy for handling the state during which the user is offline. We try
	 * to handle commands locally if possible. When unsupported commands are
	 * encountered, we offer the user the chance to connect.
	 * 
	 */
	private final class LocalStrategy extends Strategy {
		private Synchronizer localManager;

		private LocalStrategy(Synchronizer localManager) {
			this.localManager = localManager;
		}

		public void synchronize() {
			localManager.synchronize();
		}

		@Override
		State getState() {
			return State.INSTALLED;
		}

		@Override
		public LocalStrategy activate() {
			
			// ensure that's the user's authentication is persisted across sessions!
			ClientSideAuthProvider.ensurePersisted();
			
			localManager.getLastSyncTime(new AsyncCallback<Date>() {

				@Override
				public void onSuccess(Date result) {
					lastSynced = result;
					eventBus.fireEvent(new SyncCompleteEvent(result));
				
					// do an initial synchronization attempt
					localManager.synchronize();
				}

				@Override
				public void onFailure(Throwable caught) {
					localManager.synchronize();
				}
			});
			return this;
		}

		@Override
		void dispatch(Command command, AsyncCallback callback) {

			localManager.execute(command, callback);
		}
	}

	private static class CommandRequest {
		private final Command command;
		private final AsyncCallback callback;

		public CommandRequest(Command command, AsyncCallback callback) {
			super();
			this.command = command;
			this.callback = callback;
		}

		public void dispatch(Strategy strategy) {
			strategy.dispatch(command, callback);
		}
	}

	private void doDispatch(final Collection<CommandRequest> requests) {
		if (!requests.isEmpty()) {
			// wait until everything's been switched around
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {

				@Override
				public void execute() {
					for (CommandRequest request : requests) {
						request.dispatch(activeStrategy);
					}
				}
			});
		}
	}
}
