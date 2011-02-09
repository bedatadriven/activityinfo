/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

/**
 * Permits offline access to ActivityInfo with the help of Gears
 */
package org.sigmah.client.offline.ui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.RemoteDispatcher;
import org.sigmah.client.offline.OfflineGateway;
import org.sigmah.client.offline.sync.SyncStatusEvent;
import org.sigmah.client.util.state.IStateManager;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.Observable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * This class keeps as much of the offline functionality behind a runAsync clause to
 * defer downloading the related JavaScript until the user actually goes into offline mode
 *
 * @author Alex Bertram
 */
@Singleton
public class OfflinePresenter implements Dispatcher {
    /**
     * Visible for testing
     */
    static final String OFFLINE_MODE_KEY = "offline";

    /**
     * Once offline mode is installed, there are two
     * possible modes.
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
    
    public interface PromptCallback {
    	void onCancel();
    	void onTryToConnect();
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
        
        void promptToGoOnline(PromptCallback callback);
		void setConnectionDialogToFailure();
		void setConnectionDialogToBusy();
		void hideConnectionDialog();

    }

    private final View view;
    private final IStateManager stateManager;
    private final Provider<OfflineGateway> gateway;
    private final RemoteDispatcher remoteDispatcher;

    private OfflineMode activeMode;
    private boolean installed;

    private Strategy activeStrategy;


    @Inject
    public OfflinePresenter(final View view,
                            EventBus eventBus,
                            RemoteDispatcher remoteService,
                            Provider<OfflineGateway> gateway,
                            IStateManager stateManager
    ) {
        this.view = view;
        this.stateManager = stateManager;
        this.remoteDispatcher = remoteService;
        this.gateway = gateway;

        loadState();

        Log.trace("OfflineManager: starting");

        if(!installed) {
            activateStrategy(new NotInstalledStrategy());

        } else {
            activateStrategy(new LoadingOfflineStrategy());
        }

        view.getButton().addListener(Events.Select, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                activeStrategy.onDefaultButton();
            }
        });

        view.getSyncNowItem().addListener(Events.Select, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                activeStrategy.onSyncNow();
            }
        });

        view.getToggleItem().addListener(Events.Select, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                activeStrategy.onToggle();
            }
        });

        view.getReinstallItem().addListener(Events.Select, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                activeStrategy.onReinstall();
            }
        });

        eventBus.addListener(SyncStatusEvent.TYPE, new Listener<SyncStatusEvent>() {
            @Override
            public void handleEvent(SyncStatusEvent be) {
                view.updateProgress(be.getTask(), be.getPercentComplete());
            }
        });
    }

    private void loadState() {
        String state = stateManager.getString(OFFLINE_MODE_KEY);
        if(state == null) {
            installed = false;
        } else {
            installed = true;
            activeMode = OfflineMode.valueOf(state);
        }
    }

    private void setActiveMode(OfflineMode state) {
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
        } catch(Exception caught) {
            // errors really ought to be handled by the strategy that is passing control to us
            // but we can't afford to let an uncaught exception go as it could leave the app
            // in a state of limbo
            activateStrategy(new NotInstalledStrategy());
        }
    }

    private void loadGateway(final AsyncCallback<OfflineGateway> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(gateway.get());
            }
        });
    }

    private void reportFailure(Throwable throwable) {
    	view.showError(throwable.getMessage());
    }

    private abstract class Strategy {
        Strategy activate() { return this; }
        void onDefaultButton() {}
        void onSyncNow() {}
        void onToggle() {}
        void onReinstall() {}
        void dispatch(Command command, AsyncMonitor monitor, AsyncCallback callback) {
        	// by default, we send to the server
        	remoteDispatcher.execute(command, monitor, callback);
        }
    }


    /**
     * Strategy for handling the state in which offline mode is 
     * not at all available. 
     * 
     * The only thing the user can do from here is start installation.
     */
    private class NotInstalledStrategy extends Strategy {

        @Override
        public NotInstalledStrategy activate() {
            view.setButtonTextToInstall();
            return this;
        }

        @Override
        public void onDefaultButton() {
            activateStrategy(new InstallingStrategy());
        }
        
    }

    /**
     * Strategy for handling the state in which installation is in progress.
     * Commands continue to be handled by the remote dispatcher during installation
     */
    private class InstallingStrategy extends Strategy {

        @Override
        Strategy activate() {
            view.setButtonTextToInstalling();
            view.disableMenu();
            view.showProgressDialog();
            view.updateProgress("Starting...", 0);

            loadGateway(new AsyncCallback<OfflineGateway>() {
                @Override
                public void onFailure(Throwable caught) {
                    activateStrategy(new NotInstalledStrategy());
                    reportFailure(caught);
                }

                @Override
                public void onSuccess(final OfflineGateway gateway) {
                    gateway.install(new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                        	view.hideProgressDialog();
                            activateStrategy(new NotInstalledStrategy());
                            OfflinePresenter.this.reportFailure(caught);
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
     * application load when we've determined that we offline mode is 
     * installed, and that we should go offline but
     * we haven't yet loaded the async fragment with the code.
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
            
            loadGateway(new AsyncCallback<OfflineGateway>() {
                @Override
                public void onFailure(Throwable caught) {
                	abandonShip(caught);
                }

                @Override
                public void onSuccess(final OfflineGateway gateway) {
                    if(activeMode == OfflineMode.OFFLINE) {
                        gateway.goOffline(new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                abandonShip(caught);
                            }

                            @Override
                            public void onSuccess(Void result) {
                                if(gateway.validateOfflineInstalled()) {
                                	activateStrategy(new OfflineStrategy(gateway));
                                	doDispatch(pending);
                                } else {
                                    abandonShip();
                                }
                            }

						
                        });
                    }
                }
            });
            return this;
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
     * Strategy for handling the state that occurs immediately after
     * a user manually chooses to enter online mode but before 
     * we've assured that all local changes have been sent to the server.
     * 
     * We defer all subsequent command handling until we've entered
     * one mode or the other.
     */
    private class GoingOffline extends Strategy {
        private OfflineGateway offlineManager;
        
        private List<CommandRequest> pending;

        private GoingOffline(OfflineGateway offlineManager) {
            this.offlineManager = offlineManager;
        }

        @Override
        Strategy activate() {
            view.setButtonTextToSyncing();
            view.disableMenu();
            pending = new ArrayList<CommandRequest>();
            offlineManager.goOffline(new AsyncCallback<Void>() {
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
     * Strategy for handling the state during which the user is offline.
     * We try to handle commands locally if possible.
     * When unsupported commands are encountered, we offer the user the chance to connect.
     *
     */
    private class OfflineStrategy extends Strategy {
        private OfflineGateway offlineManger;

        private OfflineStrategy(OfflineGateway offlineManger) {
            this.offlineManger = offlineManger;
        }

        public OfflineStrategy activate() {
            setActiveMode(OfflineMode.OFFLINE);
            view.setButtonTextToLastSync(offlineManger.getLastSyncTime());
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

			if(offlineManger.canHandle(command)) {
				offlineManger.execute(command, monitor, callback);
			} else {
				activateStrategy(new CheckingIfUserWantsToGoOnline(offlineManger, 
						new CommandRequest(command, monitor, callback)));
			}
			
		}
       
    }
    
    private class CheckingIfUserWantsToGoOnline extends Strategy {

    	private OfflineGateway offlineManager;
    	
    	private CommandRequest toExecuteOnline;
    	private List<CommandRequest> pending;
    	
		public CheckingIfUserWantsToGoOnline(OfflineGateway offlineManger,
				CommandRequest commandRequest) {
			this.offlineManager = offlineManger;
			this.toExecuteOnline = commandRequest;
		}

		@Override
		Strategy activate() {
			pending = new ArrayList<CommandRequest>();
			view.disableMenu();			
			view.promptToGoOnline(new PromptCallback() {

				@Override
				public void onCancel() {
					if(toExecuteOnline.monitor != null) {
						toExecuteOnline.monitor.onConnectionProblem();
					}
					toExecuteOnline.callback.onFailure(new InvocationException("Not connected"));
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
						public void onFailure(Throwable arg0) {
							view.setConnectionDialogToFailure();
						}
					});
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
    

    private class GoingOnlineStrategy extends Strategy {
        private final OfflineGateway offlineManager;

        private GoingOnlineStrategy(final OfflineGateway offlineManager) {
            this.offlineManager = offlineManager;
        }

        @Override
        Strategy activate() {
            view.setButtonTextToSyncing();
            view.disableMenu();

            offlineManager.goOnline(new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    OfflinePresenter.this.reportFailure(caught);
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
    private class OnlineStrategy extends Strategy {
        private OfflineGateway offlineManager;

        private OnlineStrategy(OfflineGateway offlineManager) {
            this.offlineManager = offlineManager;
        }

        @Override
        Strategy activate() {
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
    }

    /**
     * Strategy for the period in which we are actively synchronizing the local database.
     * Synchronization can be initiated by the user when offline or offline, so we need to keep
     * track of the "parent strategy"
     */
    private class SyncingStrategy extends Strategy {
        private final Strategy parentStrategy;
        private final OfflineGateway offlineManager;

        private SyncingStrategy(final Strategy parentStrategy, OfflineGateway offlineManager) {
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
    
    private static class CommandRequest {
    	final Command command;
    	final AsyncMonitor monitor;
    	final AsyncCallback callback;
		
    	public CommandRequest(Command command, AsyncMonitor monitor,
				AsyncCallback callback) {
			super();
			this.command = command;
			this.monitor = monitor;
			this.callback = callback;
		}
    }

    private void doDispatch(final Collection<CommandRequest> requests) {
    	if(!requests.isEmpty()) {
	    	// wait until everything's been switched around 
	    	DeferredCommand.addCommand(new com.google.gwt.user.client.Command() {
				
				@Override
				public void execute() {
				   	for(CommandRequest request : requests) {
			    		activeStrategy.dispatch(request.command, request.monitor, request.callback);
			    	}				
				}
			});
    	}
    }

    
}
