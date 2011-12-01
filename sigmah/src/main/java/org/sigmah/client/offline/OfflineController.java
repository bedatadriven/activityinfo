/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

/**
 * Permits offline access to ActivityInfo with the help of Gears
 */
package org.sigmah.client.offline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Remote;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.offline.capability.OfflineCapabilityProfile;
import org.sigmah.client.offline.capability.PermissionRefusedException;
import org.sigmah.client.offline.sync.AppOutOfDateException;
import org.sigmah.client.offline.sync.SyncConnectionProblemEvent;
import org.sigmah.client.offline.sync.SyncStatusEvent;
import org.sigmah.client.offline.sync.Synchronizer;
import org.sigmah.client.offline.sync.SynchronizerConnectionException;
import org.sigmah.client.util.state.CrossSessionStateProvider;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.MutatingCommand;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.InvalidAuthTokenException;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.Observable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * This class keeps as much of the offline functionality behind a runAsync
 * clause to defer downloading the related JavaScript until the user actually
 * goes into offline mode
 * 
 * @author Alex Bertram
 */
@Singleton
public class OfflineController implements Dispatcher {
	/**
	 * Visible for testing
	 */
	static final String OFFLINE_MODE_KEY = "offline";

	/**
	 * Once offline mode is installed, there are two possible modes.
	 * 
	 */
	enum OfflineMode {
		/**
		 * Commands are handled locally
		 */
		OFFLINE,

		/**
		 * Commands are sent to the remote server for handling
		 */
		ONLINE
	}

	public interface PromptConnectCallback {
		void onCancel();

		void onTryToConnect();
	}

	public interface EnableCallback {
		void enableOffline();
	}

	public interface View {
		Observable getButton();

		Observable getSyncNowItem();

		Observable getToggleItem();

		Observable getReinstallItem();

		void setButtonTextToInstall();

		void setButtonTextToInstalling();

		void setButtonTextToLastSync(Date lastSyncTime);

		void setButtonTextToLoading();

		void setButtonTextToSyncing();

		void setButtonTextToOnline();

		void showProgressDialog();

		void updateProgress(String taskDescription, double percentComplete);

		void hideProgressDialog();

		void enableMenu();

		void disableMenu();

		void showError(String message);

		void promptToGoOnline(PromptConnectCallback callback);

		void promptToLogin();

		void setConnectionDialogToConnectionFailure();

		void setConnectionDialogToBusy();

		void setConnectionDialogToSessionExpired();

		void setConnectionDialogToServerUnavailable();

		void hideConnectionDialog();

		void showConnectionProblem(int attempt, int retryDelay);

		void promptEnable(EnableCallback callback);

		void promptToReloadForNewVersion();

		void showInstallInstructions();
		
		void showSynchronizerConnectionProblem();
	}

	private final View view;
	private final StateProvider stateManager;
	private final Provider<Synchronizer> synchronizerProvider;
	private UIConstants uiConstants;
	private final Dispatcher remoteDispatcher;
	private final OfflineCapabilityProfile capabilityProfile;

	private OfflineMode activeMode;
	private boolean installed;

	private Strategy activeStrategy;

	@Inject
	public OfflineController(final View view, EventBus eventBus,
			@Remote Dispatcher remoteDispatcher, 
			Provider<Synchronizer> gateway,
			CrossSessionStateProvider stateManager,
			OfflineCapabilityProfile capabilityProfile, 
			UIConstants uiConstants) {
		this.view = view;
		this.stateManager = stateManager;
		this.remoteDispatcher = remoteDispatcher;
		this.synchronizerProvider = gateway;
		this.capabilityProfile = capabilityProfile;
		this.uiConstants = uiConstants;

		loadState();
		if (installed) {
			validateStillSupported();
		}

		Log.trace("OfflineManager: starting");

		if (!installed) {
			NotInstalledStrategy strategy = new NotInstalledStrategy();
			activateStrategy(strategy);
			view.promptEnable(strategy);

		} else {
			activateStrategy(new LoadingOfflineStrategy());
		}

		view.getButton().addListener(Events.Select, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				activeStrategy.onDefaultButton();
			}
		});

		view.getSyncNowItem().addListener(Events.Select,
				new Listener<BaseEvent>() {
					@Override
					public void handleEvent(BaseEvent be) {
						activeStrategy.onSyncNow();
					}
				});

		view.getToggleItem().addListener(Events.Select,
				new Listener<BaseEvent>() {
					@Override
					public void handleEvent(BaseEvent be) {
						activeStrategy.onToggle();
					}
				});

		view.getReinstallItem().addListener(Events.Select,
				new Listener<BaseEvent>() {
					@Override
					public void handleEvent(BaseEvent be) {
						activeStrategy.onReinstall();
					}
				});

		eventBus.addListener(SyncStatusEvent.TYPE,
				new Listener<SyncStatusEvent>() {
					@Override
					public void handleEvent(SyncStatusEvent be) {
						view.updateProgress(be.getTask(),
								be.getPercentComplete());
					}
				});

		eventBus.addListener(SyncConnectionProblemEvent.TYPE,
				new Listener<SyncConnectionProblemEvent>() {

					@Override
					public void handleEvent(SyncConnectionProblemEvent be) {
						view.showConnectionProblem(be.getAttempt(),
								be.getRetryDelay());
					}
				});
	}

	private void loadState() {
		String state = stateManager.getString(OFFLINE_MODE_KEY);
		if (state == null) {
			installed = false;
		} else {
			installed = true;
			activeMode = OfflineMode.valueOf(state);
		}
	}

	private void validateStillSupported() {
		// can happen that the user enables off line mode,
		// and then in a future session uninstalls the required plugin
		// or revokes permission.

		// check for that here.
		if (!capabilityProfile.isOfflineModeSupported()
				|| !capabilityProfile.hasPermission()) {
			// TODO: do we display a warning here?
			installed = false;
		}
	}

	private void persistState(OfflineMode state) {
		stateManager.set(OFFLINE_MODE_KEY, state.toString());
	}

	@Override
	public <T extends CommandResult> void execute(Command<T> command,
			AsyncMonitor monitor, AsyncCallback<T> callback) {

		activeStrategy.dispatch(command, monitor, callback);

	}

	private void activateStrategy(Strategy strategy) {
		try {
			this.activeStrategy = strategy;
			this.activeStrategy.activate();
		} catch (Exception caught) {
			// errors really ought to be handled by the strategy that is passing
			// control to us
			// but we can't afford to let an uncaught exception go as it could
			// leave the app
			// in a state of limbo
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
				} catch(Exception caught) {
					Log.error("SynchronizationImpl constructor threw exception", caught);
					callback.onFailure(caught);
					return;
				}
				callback.onSuccess(impl);
			}
		});
	}

	private void reportFailure(Throwable throwable) {
		if (throwable instanceof InvalidAuthTokenException) {
			view.promptToLogin();
		} else if (throwable instanceof AppOutOfDateException
				|| throwable instanceof IncompatibleRemoteServiceException) {
			view.promptToReloadForNewVersion();

		} else if(throwable instanceof SynchronizerConnectionException) {
			view.showSynchronizerConnectionProblem();
			
		} else {
			view.showError(throwable.getMessage());
		}
	}

	private abstract class Strategy {
		Strategy activate() {
			return this;
		}

		void onDefaultButton() {
		}

		void onSyncNow() {
		}

		void onToggle() {
		}

		void onReinstall() {
		}

		void dispatch(Command command, AsyncMonitor monitor,
				AsyncCallback callback) {
			// by default, we send to the server
			remoteDispatcher.execute(command, monitor, callback);
		}
	}

	/**
	 * Strategy for handling the state in which offline mode is not at all
	 * available.
	 * 
	 * The only thing the user can do from here is start installation.
	 */
	private class NotInstalledStrategy extends Strategy implements
			EnableCallback {

		@Override
		public NotInstalledStrategy activate() {
			view.setButtonTextToInstall();
			return this;
		}

		@Override
		public void onDefaultButton() {
			if(capabilityProfile.isOfflineModeSupported()) {
				enableOffline();
			} else {
				view.showInstallInstructions();
			}
		}

		@Override
		public void enableOffline() {
			Log.trace("enablingOffline() started");
			capabilityProfile.acquirePermission(new AsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					activateStrategy(new InstallingStrategy());
				}

				@Override
				public void onFailure(Throwable caught) {
					if(!(caught instanceof PermissionRefusedException)) {
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
		Strategy activate() {
			view.setButtonTextToInstalling();
			view.disableMenu();
			view.showProgressDialog();
			view.updateProgress(uiConstants.starting(), 0);

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
							view.hideProgressDialog();
							activateStrategy(new NotInstalledStrategy());
							OfflineController.this.reportFailure(caught);
						}

						@Override
						public void onSuccess(Void result) {
							view.hideProgressDialog();
							activateStrategy(new OfflineStrategy(gateway));
						}
					});
				}
			});
			return this;
		}

		@Override
		void onDefaultButton() {
			view.showProgressDialog();
		}
	}

	/**
	 * This is a sort of purgatory state that occurs immediately after
	 * application load when we've determined that we offline mode is installed,
	 * and that we should go offline but we haven't yet loaded the async
	 * fragment with the code.
	 */
	private class LoadingOfflineStrategy extends Strategy {

		/**
		 * Commands cannot be executed until everything is loaded...
		 */
		private List<CommandRequest> pending;

		@Override
		Strategy activate() {
			pending = new ArrayList<CommandRequest>();

			view.setButtonTextToLoading();

			loadSynchronizerImpl(new AsyncCallback<Synchronizer>() {
				@Override
				public void onFailure(Throwable caught) {
					abandonShip(caught);
				}

				@Override
				public void onSuccess(final Synchronizer gateway) {
					if (activeMode == OfflineMode.ONLINE) {
						activateStrategy(new OnlineStrategy(gateway));
					} else {
						gateway.validateOfflineInstalled(new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
								abandonShip(caught);
							}

							@Override
							public void onSuccess(Void result) {
								activateStrategy(new OfflineStrategy(gateway));
								doDispatch(pending);
							}
						});
					}
				}
			});
			return this;
		}

		@Override
		void dispatch(Command command, AsyncMonitor monitor,
				AsyncCallback callback) {
			pending.add(new CommandRequest(command, monitor, callback));
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
	 * Strategy for handling the state that occurs immediately after a user
	 * manually chooses to enter online mode but before we've assured that all
	 * local changes have been sent to the server.
	 * 
	 * We defer all subsequent command handling until we've entered one mode or
	 * the other.
	 */
	private final class GoingOffline extends Strategy {
		private Synchronizer offlineManager;

		private List<CommandRequest> pending;

		private GoingOffline(Synchronizer offlineManager) {
			this.offlineManager = offlineManager;
		}

		@Override
		Strategy activate() {
			view.setButtonTextToSyncing();
			view.disableMenu();
			pending = new ArrayList<CommandRequest>();
			offlineManager.validateOfflineInstalled(new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					activateStrategy(new OnlineStrategy(offlineManager));
					doDispatch(pending);
				}

				@Override
				public void onSuccess(Void result) {
					activateStrategy(new OfflineStrategy(offlineManager));
					doDispatch(pending);
				}
			});
			return this;
		}

		@Override
		void dispatch(Command command, AsyncMonitor monitor,
				AsyncCallback callback) {
			pending.add(new CommandRequest(command, monitor, callback));
		}
	}

	/**
	 * Strategy for handling the state during which the user is offline. We try
	 * to handle commands locally if possible. When unsupported commands are
	 * encountered, we offer the user the chance to connect.
	 * 
	 */
	private final class OfflineStrategy extends Strategy {
		private Synchronizer offlineManger;

		private OfflineStrategy(Synchronizer offlineManger) {
			this.offlineManger = offlineManger;
		}

		@Override
		public OfflineStrategy activate() {
			persistState(OfflineMode.OFFLINE);
			offlineManger.getLastSyncTime(new AsyncCallback<Date>() {

				@Override
				public void onSuccess(Date result) {
					view.setButtonTextToLastSync(result);
				}

				@Override
				public void onFailure(Throwable caught) {
				}
			});
			view.enableMenu();
			return this;
		}

		@Override
		void onSyncNow() {
			activateStrategy(new SyncingStrategy(this, offlineManger));
		}

		@Override
		void onToggle() {
			activateStrategy(new GoingOnlineStrategy(offlineManger));
		}

		@Override
		void onReinstall() {
			activateStrategy(new InstallingStrategy());
		}

		@Override
		void dispatch(Command command, AsyncMonitor monitor,
				AsyncCallback callback) {

			if (offlineManger.canHandle(command)) {
				offlineManger.execute(command, monitor, callback);
			} else {
				Log.debug("Command unsupported offline: " + command);
				activateStrategy(new CheckingIfUserWantsToGoOnline(
						offlineManger, new CommandRequest(command, monitor,
								callback)));
			}
		}
	}

	private final class CheckingIfUserWantsToGoOnline extends Strategy {

		private Synchronizer offlineManager;

		private CommandRequest toExecuteOnline;
		private List<CommandRequest> pending;

		public CheckingIfUserWantsToGoOnline(Synchronizer offlineManger,
				CommandRequest commandRequest) {
			this.offlineManager = offlineManger;
			this.toExecuteOnline = commandRequest;
		}

		@Override
		Strategy activate() {
			pending = new ArrayList<CommandRequest>();
			view.disableMenu();
			view.promptToGoOnline(new PromptConnectCallback() {

				@Override
				public void onCancel() {
					if (toExecuteOnline.monitor != null) {
						toExecuteOnline.monitor.onConnectionProblem();
					}
					toExecuteOnline.callback.onFailure(new InvocationException(
							"Not connected"));
					activateStrategy(new OfflineStrategy(offlineManager));
					doDispatch(pending);
				}

				@Override
				public void onTryToConnect() {
					view.setConnectionDialogToBusy();
					offlineManager.goOnline(new AsyncCallback<Void>() {

						@Override
						public void onSuccess(Void arg0) {
							view.hideConnectionDialog();
							activateStrategy(new OnlineStrategy(offlineManager));
							doDispatch(Arrays.asList(toExecuteOnline));
							doDispatch(pending);
						}

						@Override
						public void onFailure(Throwable caught) {
							onConnectionFailure(caught);
						}
					});
				}
			});
			return this;
		}

		private void onConnectionFailure(Throwable caught) {
			Log.debug(
					"Offline Controller: goOnline failed: "
							+ caught.getMessage(), caught);
			if (caught instanceof InvalidAuthTokenException) {
				view.setConnectionDialogToSessionExpired();
			} else if (caught instanceof StatusCodeException) {
				view.setConnectionDialogToServerUnavailable();
			} else {
				view.setConnectionDialogToConnectionFailure();
			}
		}

		@Override
		void dispatch(Command command, AsyncMonitor monitor,
				AsyncCallback callback) {
			pending.add(new CommandRequest(command, monitor, callback));
		}
	}

	private final class GoingOnlineStrategy extends Strategy {
		private final Synchronizer offlineManager;

		private GoingOnlineStrategy(final Synchronizer offlineManager) {
			this.offlineManager = offlineManager;
		}

		@Override
		Strategy activate() {
			view.setButtonTextToSyncing();
			view.disableMenu();

			offlineManager.goOnline(new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					OfflineController.this.reportFailure(caught);
					activateStrategy(new OfflineStrategy(offlineManager));
				}

				@Override
				public void onSuccess(Void result) {
					activateStrategy(new OnlineStrategy(offlineManager));
				}
			});
			return this;
		}
	}

	/**
	 * Strategy for the state in which offline mode is available and installed,
	 * but we are currently connecting directly to the server.
	 */
	private final class OnlineStrategy extends Strategy {
		private Synchronizer offlineManager;

		private OnlineStrategy(Synchronizer offlineManager) {
			this.offlineManager = offlineManager;
		}

		@Override
		Strategy activate() {
			persistState(OfflineMode.ONLINE);
			view.setButtonTextToOnline();
			view.enableMenu();
			return this;
		}

		@Override
		void onSyncNow() {
			activateStrategy(new SyncingStrategy(this, offlineManager));
		}

		@Override
		void onToggle() {
			activateStrategy(new GoingOffline(offlineManager));
		}

		@Override
		void onReinstall() {
			activateStrategy(new InstallingStrategy());
		}

		@Override
		void dispatch(final Command command, AsyncMonitor monitor,
				final AsyncCallback callback) {
			
			remoteDispatcher.execute(command, monitor, new AsyncCallback() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccess(Object result) {
					callback.onSuccess(result);
					if(command instanceof MutatingCommand) {
						activateStrategy(new BackgroundSyncingWhileOnlineStrategy(offlineManager));
					}
				}			
			});
		}
	}

	/**
	 * Strategy for the period in which we are actively synchronizing the local
	 * database. Synchronization can be initiated by the user when offline or
	 * offline, so we need to keep track of the "parent strategy"
	 */
	private final class SyncingStrategy extends Strategy {
		private final Strategy parentStrategy;
		private final Synchronizer offlineManager;

		private SyncingStrategy(final Strategy parentStrategy,
				Synchronizer offlineManager) {
			this.parentStrategy = parentStrategy;
			this.offlineManager = offlineManager;
		}

		@Override
		Strategy activate() {
			view.setButtonTextToSyncing();
			view.disableMenu();
			view.showProgressDialog();

			offlineManager.synchronize(new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					reportFailure(caught);
					view.hideProgressDialog();
					activateStrategy(parentStrategy);
				}

				@Override
				public void onSuccess(Void result) {
					activateStrategy(parentStrategy);
				}
			});
			return this;
		}

		@Override
		void onDefaultButton() {
			view.showProgressDialog();
		}

		@Override
		void dispatch(Command command, AsyncMonitor monitor,
				AsyncCallback callback) {

			// defer to our parent strategy
			parentStrategy.dispatch(command, monitor, callback);
		}
	}
	
	private final class BackgroundSyncingWhileOnlineStrategy extends Strategy {
		private final Synchronizer synchronizer;
		private boolean dirty = false;
		
		private BackgroundSyncingWhileOnlineStrategy(Synchronizer offlineManager) {
			this.synchronizer = offlineManager;
		}

		@Override
		Strategy activate() {
			view.setButtonTextToSyncing();
			view.disableMenu();

			startSynchronization();
			
			return this;
		}

		private void startSynchronization() {
			synchronizer.synchronize(new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					recordInconsistentState();
					reportFailure(caught);
					view.hideProgressDialog();
					activateStrategy(new OnlineStrategy(synchronizer));
				}

				@Override
				public void onSuccess(Void result) {
					if(dirty) {
						// while we have been synchronzing, the user a sent a command
						// to the server that mutated the remote state,
						// so we need to synchronize again to fetch the changes
						startSynchronization();
					} else {
						activateStrategy(new OnlineStrategy(synchronizer));
					}
				}
			});
		}

		private void recordInconsistentState() {
			
		}

		@Override
		void onDefaultButton() {
			view.showProgressDialog();
		}

		@Override
		void dispatch(final Command command, AsyncMonitor monitor,
				final AsyncCallback callback) {

			remoteDispatcher.execute(command, monitor, new AsyncCallback() {
				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccess(Object result) {
					if(command instanceof MutatingCommand) {
						dirty = true;
					}
					callback.onSuccess(result);
				}
			});
		}		
	}

	private static class CommandRequest {
		private final Command command;
		private final AsyncMonitor monitor;
		private final AsyncCallback callback;

		public CommandRequest(Command command, AsyncMonitor monitor,
				AsyncCallback callback) {
			super();
			this.command = command;
			this.monitor = monitor;
			this.callback = callback;
		}

		public void dispatch(Strategy strategy) {
			strategy.dispatch(command, monitor, callback);
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
