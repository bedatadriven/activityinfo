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
import org.activityinfo.client.local.LocalStateChangeEvent;
import org.activityinfo.client.local.LocalStateChangeEvent.State;
import org.activityinfo.client.local.sync.SyncCompleteEvent;
import org.activityinfo.client.local.sync.SyncStatusEvent;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Status;
import com.google.inject.Inject;

public class WorkStatus extends Status {

    private boolean syncing;
    private State state = State.UNINSTALLED;

    @Inject
    public WorkStatus(EventBus eventBus) {

        eventBus.addListener(SyncStatusEvent.TYPE,
            new Listener<SyncStatusEvent>() {

                @Override
                public void handleEvent(SyncStatusEvent be) {
                    setBusy(be.getTask() + " "
                        + ((int) (be.getPercentComplete())) + "%");
                    syncing = true;
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

    private void onOfflineStatusChange(State state) {
        this.state = state;
        if (!syncing) {
            clearBusy();
        }
    }

    private void clearBusy() {
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

}
