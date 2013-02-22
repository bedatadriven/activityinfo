package org.activityinfo.client.dispatch;

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

import org.activityinfo.shared.command.Command;

/**
 * Interface to a class that which emits Dispatch-related events.
 * 
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface DispatchEventSource {
    /**
     * Registers a DispatchListener with the source. DispatchListeners are
     * called before and after commands are dispatched.
     * 
     * @param commandClass
     *            The class of Command for which to listen
     * @param listener
     * @param <T>
     *            A Command class implementing
     *            {@link org.activityinfo.shared.command.Command}
     */
    <T extends Command> void registerListener(Class<T> commandClass,
        DispatchListener<T> listener);

    /**
     * Registers a CommandProxy with the source. Command proxies are called
     * before commands are sent to the server.
     * 
     * @param commandClass
     *            The class of Command for which to proxy
     * @param proxy
     * @param <T>
     *            A Command class implementing
     *            {@link org.activityinfo.shared.command.Command}
     */
    <T extends Command> void registerProxy(Class<T> commandClass,
        CommandCache<T> proxy);
}
