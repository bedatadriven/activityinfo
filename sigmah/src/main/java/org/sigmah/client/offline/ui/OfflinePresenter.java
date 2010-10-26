/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

/**
 * Permits offline access to ActivityInfo with the help of Gears
 */
package org.sigmah.client.offline.ui;


import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.Observable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.sigmah.client.EventBus;
import org.sigmah.client.offline.OfflineGateway;
import org.sigmah.client.offline.sync.SyncStatusEvent;
import org.sigmah.client.util.state.IStateManager;

import java.util.Date;

/**
 * This class keeps as much of the offline functionality behind a runAsync clause to
 * defer downloading the related JavaScript until the user actually goes into offline mode
 *
 * @author Alex Bertram
 */
public class OfflinePresenter {
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

    }

    private final View view;
    private final IStateManager stateManager;
    private final Provider<OfflineGateway> gateway;

    private OfflineMode activeMode;
    private boolean installed;

    private Strategy activeStrategy;


    @Inject
    public OfflinePresenter(final View view,
                            EventBus eventBus,
                            Provider<OfflineGateway> gateway,
                            IStateManager stateManager
    ) {
        this.view = view;
        this.stateManager = stateManager;
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

    private void activateStrategy(Strategy strategy) {
        this.activeStrategy = strategy;
        this.activeStrategy.activate();
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
        // TODO
    }

    private abstract class Strategy {
        Strategy activate() { return this; }
        void onDefaultButton() {}
        void onSyncNow() {}
        void onToggle() {}
        void onReinstall() {}
    }


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
     * UX behavior whilst installing
     */
    private class InstallingStrategy extends Strategy {

        @Override
        Strategy activate() {
            view.setButtonTextToInstalling();
            view.disableMenu();
            view.showProgressDialog();

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
     * application load when we've determined that we should go offline but
     * we haven't yet loaded the async fragment with the code.
     */
    private class LoadingOfflineStrategy extends Strategy {

        @Override
        Strategy activate() {
            view.setButtonTextToLoading();
            loadGateway(new AsyncCallback<OfflineGateway>() {
                @Override
                public void onFailure(Throwable caught) {
                    activateStrategy(new NotInstalledStrategy());
                    reportFailure(caught);
                }

                @Override
                public void onSuccess(final OfflineGateway gateway) {
                    if(activeMode == OfflineMode.OFFLINE) {
                        gateway.goOffline(new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                activateStrategy(new NotInstalledStrategy());
                                reportFailure(caught);
                            }

                            @Override
                            public void onSuccess(Void result) {
                                activateStrategy(new OfflineStrategy(gateway));
                            }
                        });
                    }
                }
            });
            return this;
        }
    }

    private class GoingOffline extends Strategy {
        private OfflineGateway offlineManager;

        private GoingOffline(OfflineGateway offlineManager) {
            this.offlineManager = offlineManager;
        }

        @Override
        Strategy activate() {
            view.setButtonTextToSyncing();
            view.disableMenu();
            offlineManager.goOffline(new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    activateStrategy(new OnlineStrategy(offlineManager));
                }

                @Override
                public void onSuccess(Void result) {
                    activateStrategy(new OfflineStrategy(offlineManager));
                }
            });
            return this;
        }
    }

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
    }
}
