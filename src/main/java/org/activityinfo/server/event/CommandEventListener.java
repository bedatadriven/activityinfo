package org.activityinfo.server.event;

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

import java.util.logging.Logger;

import org.activityinfo.shared.command.Command;

import com.google.common.eventbus.Subscribe;

@SuppressWarnings("rawtypes")
public abstract class CommandEventListener {
    protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    private Class<? extends Command>[] triggers;

    public CommandEventListener(ServerEventBus serverEventBus,
        Class<? extends Command>... triggers) {
        serverEventBus.register(this);
        this.triggers = triggers;
    }

    @Subscribe
    public void handleEvent(CommandEvent event) {
        Class<? extends Command> commandClass = event.getCommand().getClass();

        for (Class<?> trigger : triggers) {
            if (trigger.isAssignableFrom(commandClass)) {
                LOGGER.fine("handler " + this.getClass().getSimpleName()
                    + " triggered by command " + event.getCommand());
                try {
                    onEvent(event);
                } catch (Exception e) {
                    LOGGER.warning("couldn't handle command "
                        + commandClass.getSimpleName() + ": " + e.getMessage());
                    LOGGER.throwing(this.getClass().getSimpleName(),
                        "handleEvent", e);
                }
            }
        }
    }

    protected abstract void onEvent(CommandEvent event);
}
