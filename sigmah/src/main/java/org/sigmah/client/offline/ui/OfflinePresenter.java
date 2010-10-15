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
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.sigmah.client.EventBus;
import org.sigmah.client.offline.OfflineGateway;
import org.sigmah.client.offline.OfflineStatus;
import org.sigmah.client.offline.sync.SyncStatusEvent;

import java.util.Date;

/**
 * This class keeps as much of the offline functionality behind a runAsync clause to
 * defer downloading the related JavaScript until the user actually goes into offline mode
 *
 * @author Alex Bertram
 */
public class OfflinePresenter {
    private final View view;
    private final OfflineStatus offlineStatus;
    private final Provider<OfflineGateway> gateway;

    public interface View {
        Observable getInstallButton();
        Observable getSyncNowButton();
        Observable getToggleOfflineButton();

        void setInstalled(boolean installed);
        void setOffline(boolean offline);
        void setProgress(String taskDescription, double percentComplete);
        void setLastSyncDate(Date date);
    }

    @Inject
    public OfflinePresenter(final View view,
                          final OfflineStatus status,
                          EventBus eventBus,
                          OfflineStatus offlineStatus,
                          Provider<OfflineGateway> gateway) {
        this.view = view;
        this.offlineStatus = offlineStatus;
        this.gateway = gateway;

        Log.trace("OfflineManager: starting");

        this.view.setInstalled(status.isOfflineInstalled());

        // if the user was offline in their last session,
        // go offline now
        if(status.isOfflineEnabled()) {
            goOffline();
        }

        this.view.getInstallButton().addListener(Events.Select, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent baseEvent) {
                if(!status.isOfflineInstalled()) {
                    installOffline();
                }
            }
        });

        this.view.getSyncNowButton().addListener(Events.Select, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
               syncNow();
            }
        });

        this.view.getToggleOfflineButton().addListener(Events.Select, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if(!status.isOfflineEnabled()) {
                    goOffline();
                } else {
                    goOnline();
                }
            }
        });

        eventBus.addListener(SyncStatusEvent.TYPE, new Listener<SyncStatusEvent>() {
            @Override
            public void handleEvent(SyncStatusEvent be) {
                view.setProgress(be.getTask(), be.getPercentComplete());
            }
        });
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

    private void goOffline() {

        loadGateway(new AsyncCallback<OfflineGateway>() {
            @Override
            public void onFailure(Throwable throwable) {
                OfflinePresenter.this.onFailure(throwable);
            }

            @Override
            public void onSuccess(OfflineGateway gateway) {
                gateway.goOffline(new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        OfflinePresenter.this.onFailure(throwable);
                    }

                    @Override
                    public void onSuccess(Void voidValue) {
                        offlineStatus.setOfflineEnabled(true);
                        view.setOffline(true);
                    }
                });
            }
        });
    }


    private void installOffline() {
        Factory gearsFactory = Factory.getInstance();
        if(gearsFactory == null) {
            offlineNotSupported();
            return;
        }

        if(!gearsFactory.hasPermission() && !gearsFactory.hasPermission()) {
            return;
        }

        loadGateway(new AsyncCallback<OfflineGateway>() {
            @Override
            public void onFailure(Throwable caught) {
                OfflinePresenter.this.onFailure(caught);
            }

            @Override
            public void onSuccess(OfflineGateway result) {
                result.install(new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        OfflinePresenter.this.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        offlineStatus.setOfflineEnabled(true);
                        view.setInstalled(true);
                        view.setOffline(true);
                    }
                });
            }
        });
    }

    private void goOnline() {
        loadGateway(new AsyncCallback<OfflineGateway>() {
            @Override
            public void onFailure(Throwable throwable) {
                OfflinePresenter.this.onFailure(throwable);
            }

            @Override
            public void onSuccess(OfflineGateway gateway) {
                gateway.goOnline(new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        OfflinePresenter.this.onFailure(throwable);
                    }

                    @Override
                    public void onSuccess(Void aVoid) {
                        offlineStatus.setOfflineEnabled(false);
                        view.setOffline(false);
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

    private boolean isOfflineSupported() {
        return Factory.getInstance() != null;
    }

    private void offlineNotSupported() {
        MessageBox.confirm("Offline Mode", "ActivityInfo currently requires the Gears plugin to function offline." +
                " Would you like to download the plugin now?", new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                Window.open("http://gears.google.com", "_blank", null);
            }
        });
    }

    private void onFailure(Throwable throwable) {
        // TODO
    }
}
