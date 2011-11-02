/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.dozer.Mapper;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.database.hibernate.entity.UserDatabase;
import org.sigmah.server.endpoint.gwtrpc.ServerExecutionContext;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.Month;
import org.sigmah.shared.command.handler.AuthorizationHandler;
import org.sigmah.shared.command.handler.CommandHandlerAsync;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Injector;

/**
 * Convenience methods for <code>CommandHandler</code>s
 */
public class HandlerUtil {


    /**
     * Returns the <code>CommandHandler</code> that corresponds to the given <code>Command</code>.
     * <strong>Only</strong> the package org.sigmah.server.command.handler
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
    				   
    	handlerName = "org.sigmah.server.command.handler." +
    		commandName + "Handler";

        try {
            return (Class<CommandHandler<?>>) CommandHandler.class.getClassLoader().loadClass(handlerName);

        } catch (ClassNotFoundException e1) {
        	
        	// try looking for the handler in the shared package
        				   
        	handlerName = "org.sigmah.shared.command.handler." +
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
     * <strong>Only</strong> the package org.sigmah.server.command.handler
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
    	
    	sharedHandlerName = "org.sigmah.shared.command.handler." +
    		commandName + "Handler";

        try {
            return (Class<CommandHandlerAsync<C,R>>) CommandHandler.class.getClassLoader().loadClass(sharedHandlerName);

        } catch (ClassNotFoundException e) {
        	String serverHandlerName = "org.sigmah.server.command.handler." +
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
    	
    	handlerName = "org.sigmah.server.command.authorization." +
    		commandName + "AuthorizationHandler";

        try {
            return (Class<AuthorizationHandler<C>>) CommandHandler.class.getClassLoader().loadClass(handlerName);

        } catch (ClassNotFoundException e1) {
        	return null;
        }
    }


    public static <T extends CommandResult> T execute(Injector injector, Command<T> cmd, User user) throws CommandException {
        Class handlerClass = handlerForCommand(cmd);
        Object  handler = injector.getInstance(handlerClass);
        if(handler instanceof CommandHandler) {
        	CommandHandler syncHandler = (CommandHandler)handler;
        	return (T) syncHandler.execute(cmd, user);	
        } else {
        	return (T)ServerExecutionContext.execute(injector, cmd);
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
