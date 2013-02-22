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
import org.activityinfo.client.local.command.CommandQueueEvent;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Status;
import com.google.inject.Inject;

public class CommandQueueStatus extends Status {

    @Inject
    public CommandQueueStatus(EventBus eventBus) {
        eventBus.addListener(CommandQueueEvent.TYPE,
            new Listener<CommandQueueEvent>() {

                @Override
                public void handleEvent(CommandQueueEvent be) {
                    if (be.getEnqueuedItemCount() > 0) {
                        setText(be.getEnqueuedItemCount() + " changes pending");
                        setBox(true);
                    } else {
                        setText(null);
                        setBox(false);
                    }
                }
            });
    }
}
