
package org.activityinfo.server.command.handler;

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



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.endpoint.gwtrpc.RemoteExecutionContext;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.Month;
import org.activityinfo.shared.command.handler.AuthorizationHandler;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.dozer.Mapper;

import com.google.inject.Injector;

/**
 * Convenience methods for <code>CommandHandler</code>s
 */
public final class HandlerUtil {

	private HandlerUtil() {}

    /**
     * Returns the <code>CommandHandler</code> that corresponds to the given <code>Command</code>.
     * <strong>Only</strong> the package org.activityinfo.server.command.handler
     * is searched, so the handler must be there.
     *
     * @param cmd The <code>Command</code> for which a <code>CommandHandler</code> is to be returned
     * @return A <code>CommandHandler</code> capabling of handling the given <code>Command</code>
     */
    @SuppressWarnings("unchecked")
    public static Class handlerForCommand(Command<?> cmd) {

        String commandName = cmd.getClass().getName().substring(
                cmd.getClass().getPackage().getName().length() + 1);
    	String handlerName = null;
    				   
    	handlerName = "org.activityinfo.server.command.handler." +
    		commandName + "Handler";

        try {
            return CommandHandler.class.getClassLoader().loadClass(handlerName);

        } catch (ClassNotFoundException e1) {
        	
        	// try looking for the handler in the shared package
        				   
        	handlerName = "org.activityinfo.shared.command.handler." +
        			commandName + "Handler";
        	
        	try {
        		return Class.forName(handlerName);
        	} catch(ClassNotFoundException e2) {
        	
        		throw new IllegalArgumentException("No handler " + handlerName + " found for " + commandName, e2);
        	}
        }

    }
    

    /**
     * Returns the <code>CommandHandler</code> that corresponds to the given <code>Command</code>.
     * <strong>Only</strong> the package org.activityinfo.server.command.handler
     * is searched, so the handler must be there.
     *
     * @param cmd The <code>Command</code> for which a <code>CommandHandler</code> is to be returned
     * @return A <code>CommandHandler</code> capabling of handling the given <code>Command</code>
     */
    @SuppressWarnings("unchecked")
    public static <C extends Command<R>, R extends CommandResult> Class<CommandHandlerAsync<C,R>> 
    			asyncHandlerForCommand(C cmd) {

        String commandName = cmd.getClass().getName().substring(
                cmd.getClass().getPackage().getName().length() + 1);
    	String sharedHandlerName = null;
    	
    	sharedHandlerName = "org.activityinfo.shared.command.handler." +
    		commandName + "Handler";

        try {
            return (Class<CommandHandlerAsync<C,R>>) CommandHandler.class.getClassLoader().loadClass(sharedHandlerName);

        } catch (ClassNotFoundException e) {
        	String serverHandlerName = "org.activityinfo.server.command.handler." +
    			commandName + "Handler";
        	try {
        		return (Class<CommandHandlerAsync<C,R>>) CommandHandler.class.getClassLoader().loadClass(serverHandlerName);
        	} catch (Exception ex) {
        		throw new IllegalArgumentException("No async handler " + serverHandlerName + " found for " + commandName, e);
        	}
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <C extends Command<?>> Class<AuthorizationHandler<C>> authorizationHandlerForCommand(C cmd) {

        String commandName = cmd.getClass().getName().substring(
                cmd.getClass().getPackage().getName().length() + 1);
    	String handlerName = null;
    	
    	handlerName = "org.activityinfo.server.command.authorization." +
    		commandName + "AuthorizationHandler";

        try {
            return (Class<AuthorizationHandler<C>>) CommandHandler.class.getClassLoader().loadClass(handlerName);

        } catch (ClassNotFoundException e1) {
        	return null;
        }
    }


    public static <T> List<T> mapList(Mapper mapper, Collection<?> source, Class<T> destinationClass) {
        List<T> list = new ArrayList<T>(source.size());
        for (Object s : source) {
            list.add(mapper.map(s, destinationClass));
        }
        return list;
    }

    static Month monthFromRange(Date date1, Date date2) {

        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        if (c1.get(Calendar.DAY_OF_MONTH) != 1) {
            return null;
        }

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        if (c2.get(Calendar.DAY_OF_MONTH) != c2.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            return null;
        }

        if (c2.get(Calendar.MONTH) != c1.get(Calendar.MONTH) ||
                c2.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) {

            return null;
        }

        return new Month(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH) + 1);

    }
}
