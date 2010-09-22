/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

/**
 * Permits offline access to ActivityInfo with the help of Gears
 */
package org.sigmah.client.offline;


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
import org.sigmah.client.i18n.I18N;

/**
 * This class keeps as much of the offline functionality behind a runAsync clause to
 * defer downloading the related JavaScript until the user actually goes into offline mode
 *
 * @author Alex Bertram
 */
public class OfflineManager {
    private final View view;
    private final OfflineStatus offlineStatus;
    private final Provider<OfflineGateway> gateway;

    private boolean offline = false;

    public interface View {
        Observable getEnableOfflineModeMenuItem();
        void setOfflineModeMenuItemText(String text);
        void setOfflineModeMenuText(String text);
    }

    @Inject
    public OfflineManager(final View view,
                          final OfflineStatus status,
                          OfflineStatus offlineStatus, Provider<OfflineGateway> gateway) {
        this.view = view;
        this.offlineStatus = offlineStatus;
        this.gateway = gateway;

        Log.trace("OfflineManager: starting");

        this.view.setOfflineModeMenuItemText(I18N.CONSTANTS.enableOffline());
        this.view.setOfflineModeMenuText(I18N.CONSTANTS.offlineModeOnlineOnly());

        // if the user was offline in their last session,
        // go offline now
        if(status.isOfflineEnabled()) {
            goOffline();
        }

        this.view.getEnableOfflineModeMenuItem().addListener(Events.Select, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent baseEvent) {
                if(!status.isOfflineInstalled()) {
                    installOffline();
                } else if(!status.isOfflineEnabled()) {
                    goOffline();
                } else {
                    goOnline();
                }
            }
        });
    }

    private void goOffline() {

        loadGateway(new AsyncCallback<OfflineGateway>() {
            @Override
            public void onFailure(Throwable throwable) {
                OfflineManager.this.onFailure(throwable);
            }

            @Override
            public void onSuccess(OfflineGateway gateway) {
                gateway.goOffline(new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        OfflineManager.this.onFailure(throwable);
                    }

                    @Override
                    public void onSuccess(Void voidValue) {
                        offlineStatus.setOfflineEnabled(true);
                        view.setOfflineModeMenuItemText(I18N.CONSTANTS.reloadOffline());
                        view.setOfflineModeMenuText(I18N.CONSTANTS.offlineMode());
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
                OfflineManager.this.onFailure(caught);
            }

            @Override
            public void onSuccess(OfflineGateway result) {
                result.install(new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        OfflineManager.this.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        offlineStatus.setOfflineEnabled(true);
                        view.setOfflineModeMenuItemText(I18N.CONSTANTS.enableOffline());
                        view.setOfflineModeMenuText(I18N.CONSTANTS.offlineModeOnlineOnly());
                    }
                });
            }
        });
    }

    private void goOnline() {
        loadGateway(new AsyncCallback<OfflineGateway>() {
            @Override
            public void onFailure(Throwable throwable) {
                OfflineManager.this.onFailure(throwable);
            }

            @Override
            public void onSuccess(OfflineGateway gateway) {
                gateway.goOnline(new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        OfflineManager.this.onFailure(throwable);
                    }

                    @Override
                    public void onSuccess(Void aVoid) {
                        offlineStatus.setOfflineEnabled(false);
                        view.setOfflineModeMenuItemText(I18N.CONSTANTS.enableOffline());
                        view.setOfflineModeMenuText(I18N.CONSTANTS.offlineModeOnlineOnly());
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
