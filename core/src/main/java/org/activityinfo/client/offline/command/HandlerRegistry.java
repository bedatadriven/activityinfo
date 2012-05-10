package org.activityinfo.client.offline.command;

import java.util.HashMap;
import java.util.Map;

import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.inject.Singleton;


@Singleton
public final class HandlerRegistry {
    private final Map<Class, CommandHandlerAsync> map = new HashMap<Class, CommandHandlerAsync>();


    public <C extends Command<R>, R extends CommandResult> void registerHandler(Class<C> commandClass, CommandHandlerAsync<C,R> handler) {
        map.put(commandClass, handler);
    }

    public <C extends Command<R>,R extends CommandResult> CommandHandlerAsync<C,R> getHandler(C c) {
        CommandHandlerAsync<C,R> handler = map.get(c.getClass());
        if(handler == null) {
            throw new IllegalArgumentException("No handler class for " + c.toString());
        }

        return handler;
    }
    
    public boolean hasHandler(Command<?> command) {
    	return map.containsKey(command.getClass());
    }

}
