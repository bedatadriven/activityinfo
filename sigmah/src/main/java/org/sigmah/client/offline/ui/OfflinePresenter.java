/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

/**
 * Permits offline access to ActivityInfo with the help of Gears
 */
package org.sigmah.client.offline.ui;


import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.*;
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
        static String SYNC_NOW_ID = "syncNow";
        static String TOGGLE_ID = "toggle";

        Observable getButton();
        Observable getMenu();

        void setButtonTextToInstall();
        void setButtonTextToInstalling();
        void setButtonTextToLastSync(Date lastSyncTime);
        void setButtonTextToLoading();
        void setButtonTextToSyncing();

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
            activeStrategy = new NotInstalledStrategy().activate();

        } else {
            activeStrategy = new LoadingStrategy();
        }

        view.getButton().addListener(Events.Select, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                activeStrategy.onDefaultButton();
            }
        });

        view.getButton().addListener(Events.Select, new Listener<MenuEvent>() {
            @Override
            public void handleEvent(MenuEvent event) {
                activeStrategy.onMenuItemSelected(event.getItem().getItemId());
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

    private void syncNow() {
        loadGateway(new AsyncCallback<OfflineGateway>() {
            @Override
            public void onFailure(Throwable caught) {
                OfflinePresenter.this.onFailure(caught);
            }

            @Override
            public void onSuccess(OfflineGateway result) {
                result.synchronize(new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(Void result) {

                    }
                });
            }
        });
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

    private void onFailure(Throwable throwable) {
        // TODO
    }

    private abstract class Strategy {
        Strategy activate() { return this; }
        void onDefaultButton() {}
        void onMenuItemSelected(String itemId) {}
    }


    private class NotInstalledStrategy extends Strategy {

        @Override
        public NotInstalledStrategy activate() {
            view.setButtonTextToInstall();
            return this;
        }

        @Override
        public void onDefaultButton() {

            activeStrategy = new InstallingStrategy();

            loadGateway(new AsyncCallback<OfflineGateway>() {
                @Override
                public void onFailure(Throwable caught) {
                    activeStrategy = activate();
                    OfflinePresenter.this.onFailure(caught);
                }

                @Override
                public void onSuccess(final OfflineGateway gateway) {
                    gateway.install(new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            activeStrategy = activate();
                            OfflinePresenter.this.onFailure(caught);
                        }

                        @Override
                        public void onSuccess(Void result) {
                            view.hideProgressDialog();
                            activeStrategy = new OfflineStrategy(gateway).activate();
                        }
                    });
                }
            });
        }
    }

    /**
     * UX behavior whilst installing
     */
    private class InstallingStrategy extends Strategy {

        private InstallingStrategy() {
            view.setButtonTextToInstalling();
            view.disableMenu();
            view.showProgressDialog();
        }
    }

    private class LoadingStrategy extends Strategy {

        private LoadingStrategy() {
            view.setButtonTextToLoading();
            loadGateway(new AsyncCallback<OfflineGateway>() {
                @Override
                public void onFailure(Throwable caught) {
                    //TODO
                }

                @Override
                public void onSuccess(final OfflineGateway gateway) {
                    if(activeMode == OfflineMode.OFFLINE) {
                        gateway.goOffline(new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                // TODO
                            }

                            @Override
                            public void onSuccess(Void result) {
                                activeStrategy = new OfflineStrategy(gateway).activate();
                            }
                        });
                    }
                }
            });
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
        void onMenuItemSelected(String itemId) {
            if(View.SYNC_NOW_ID.equals(itemId)) {
                activeStrategy = new SyncingStrategy(this, offlineManger);
            }
        }
    }

    private class SyncingStrategy extends Strategy {
        private SyncingStrategy(final Strategy parentStrategy, OfflineGateway offlineManager) {
            view.setButtonTextToSyncing();
            view.disableMenu();

            offlineManager.synchronize(new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    activeStrategy = parentStrategy.activate();
                }

                @Override
                public void onSuccess(Void result) {
                    activeStrategy = parentStrategy.activate();
                }
            });
        }
    }
}
