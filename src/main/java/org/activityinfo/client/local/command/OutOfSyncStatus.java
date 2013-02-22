package org.activityinfo.client.local.command;

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
import org.activityinfo.client.local.ui.SyncStatusResources;

import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.tips.ToolTip;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class OutOfSyncStatus extends Status implements OutOfSyncMonitor.View {

    private ToolTip tip = null;

    @Inject
    public OutOfSyncStatus(EventBus eventBus) {
        new OutOfSyncMonitor(eventBus, this);
        createToolTip();
    }

    private void createToolTip() {
        if (this.tip == null) {
            ToolTipConfig config = new ToolTipConfig();
            config.setTitle("Out of Sync");
            config
                .setText("You have recently sent an update directly to the server.<br>"
                    +
                    "Your change will not be visible locally until you synchronize.");
            config.setShowDelay(1);

            this.tip = new ToolTip(this, config);
        }
    }

    @Override
    public void show() {
        setIconStyle(SyncStatusResources.INSTANCE.style().warningIcon());
        setText("Your local copy is out sync with the sever");
        setBox(true);
        createTip();
    }

    @Override
    public void hide() {
        setBox(false);
        setIconStyle(null);
        setText(null);
    }

    private ToolTip createTip() {
        ToolTipConfig config = new ToolTipConfig();
        config.setAnchor("top");
        config.setCloseable(false);
        config
            .setText("Your local database is out of sync with the server. Please wait while synchronization completes");

        ToolTip tip = new ToolTip();
        tip.update(config);

        return tip;
    }

}
