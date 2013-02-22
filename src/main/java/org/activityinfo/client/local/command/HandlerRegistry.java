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

import java.util.HashMap;
import java.util.Map;

import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.inject.Singleton;

@Singleton
public final class HandlerRegistry {
    private final Map<Class, CommandHandlerAsync> map = new HashMap<Class, CommandHandlerAsync>();

    public <C extends Command<R>, R extends CommandResult> void registerHandler(
        Class<C> commandClass, CommandHandlerAsync<C, R> handler) {
        map.put(commandClass, handler);
    }

    public <C extends Command<R>, R extends CommandResult> CommandHandlerAsync<C, R> getHandler(
        C c) {
        CommandHandlerAsync<C, R> handler = map.get(c.getClass());
        if (handler == null) {
            throw new IllegalArgumentException("No handler class for "
                + c.toString());
        }

        return handler;
    }

    public boolean hasHandler(Command<?> command) {
        return map.containsKey(command.getClass());
    }

}
