package org.activityinfo.client.local.ui;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.EventBus;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.local.LocalStateChangeEvent;
import org.activityinfo.client.local.LocalStateChangeEvent.State;
import org.activityinfo.client.local.sync.SyncCompleteEvent;
import org.activityinfo.client.local.sync.SyncErrorEvent;
import org.activityinfo.client.local.sync.SyncErrorType;
import org.activityinfo.client.local.sync.SyncStatusEvent;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.google.gwt.user.client.Event;
import com.google.inject.Inject;

public class WorkStatus extends Status {

    private boolean syncing;
    private State state = State.UNINSTALLED;
    protected String lastErrorMessage;

    @Inject
    public WorkStatus(EventBus eventBus) {

        eventBus.addListener(SyncStatusEvent.TYPE,
            new Listener<SyncStatusEvent>() {

                @Override
                public void handleEvent(SyncStatusEvent be) {
                    setBusy(be.getTask() + " "  + ((int) (be.getPercentComplete())) + "%");
                    syncing = true;
                }
            });
        
        eventBus.addListener(SyncErrorEvent.TYPE, new Listener<SyncErrorEvent>() {

            @Override
            public void handleEvent(SyncErrorEvent event) {
                warn("Sync error");
                lastErrorMessage = formatErrorMessage(event.getErrorType());
            }
        });
        eventBus.addListener(SyncCompleteEvent.TYPE,
            new Listener<SyncCompleteEvent>() {

                @Override
                public void handleEvent(SyncCompleteEvent event) {
                    clearBusy();
                }
            });

        eventBus.addListener(LocalStateChangeEvent.TYPE,
            new Listener<LocalStateChangeEvent>() {

                @Override
                public void handleEvent(LocalStateChangeEvent be) {
                    onOfflineStatusChange(be.getState());
                }
            });
        
    }
        
    private void warn(String warning) {
        setIconStyle(SyncStatusResources.INSTANCE.style().warningIcon());
        addStyleName(SyncStatusResources.INSTANCE.style().warning());
        setText(warning);
    }
    
    private void clearWarning() {
        removeStyleName(SyncStatusResources.INSTANCE.style().warning());   
        lastErrorMessage = null;
    }

    @Override
    protected void afterRender() {
        super.afterRender();
        el().addEventsSunk(Event.ONCLICK);
    }

    private String formatErrorMessage(SyncErrorType type) {
        switch(type) {
        case APPCACHE_TIMEOUT:
            if(GXT.isChrome) {
                return I18N.CONSTANTS.syncAppCacheChrome();
            } else {
                return I18N.CONSTANTS.syncErrorConnection();
            }
        case CONNECTION_PROBLEM:
            return I18N.CONSTANTS.syncErrorConnection();
        case INVALID_AUTH:
            return I18N.CONSTANTS.syncErrorAuth();
        case NEW_VERSION:
            return I18N.CONSTANTS.syncErrorReload();
        default:
        case UNEXPECTED_EXCEPTION:
            return I18N.CONSTANTS.syncErrorUnexpected();
        }
    }

    private void onOfflineStatusChange(State state) {
        this.state = state;
        if (!syncing) {
            clearBusy();
        }
    }

    private void clearBusy() {
        clearWarning();

        switch (state) {
        case UNINSTALLED:
        case INSTALLING:
            this.clearStatus("Working online");
            break;
        case CHECKING:
            this.clearStatus("Loading...");
            break;
        case INSTALLED:
            this.clearStatus("Working offline");
            break;
        }
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        
        if(event.getTypeInt() == Event.ONCLICK && lastErrorMessage != null) {
            MessageBox.alert("Synchronization Error", lastErrorMessage, new Listener<MessageBoxEvent>() {
                
                @Override
                public void handleEvent(MessageBoxEvent be) {
                    clearBusy();
                    clearWarning();
                }
            });
        }
    }
}
