package org.activityinfo.server.command.handler;


import java.util.*;

import org.activityinfo.server.domain.UserDatabase;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.Month;
import org.dozer.Mapper;

public class HandlerUtil {

    protected Class classForEntityName(String name) throws ClassNotFoundException {

        if (name.equals("Database")) {
            return UserDatabase.class;
        }

        return CommandHandler.class.getClassLoader().loadClass("org.activityinfo.server.domain." + name);
    }

    /**
     * Returns the command executor that corresponds to the given command.
     * <strong>Only</strong> the package com.bertram.activityinfo.server.command.handler
     * is searched, so the executor must be there.
     *
     * @param cmd
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Class<CommandHandler<?>> executorForCommand(Command<?> cmd) {

        String commandName = cmd.getClass().getName().substring(
                cmd.getClass().getPackage().getName().length() + 1);

        String etorName = CommandHandler.class.getPackage().getName() + "." +
                commandName + "Handler";

        try {

            return (Class<CommandHandler<?>>) CommandHandler.class.getClassLoader().loadClass(etorName);

        } catch (ClassNotFoundException e) {

            throw new IllegalArgumentException("No handler class for " + commandName, e);

        }
    }


    public static <T> List<T> mapList(Mapper mapper, Collection<?> source, Class<T> destinationClass ) {
        List<T> list = new ArrayList<T>(source.size());
        for(Object s : source) {
            list.add(mapper.map(s, destinationClass));
        }
        return list;
    }

    static Month monthFromRange(Date date1, Date date2) {

        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        if(c1.get(Calendar.DAY_OF_MONTH) != 1) {
            return null;
        }

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        if(c2.get(Calendar.DAY_OF_MONTH) != c2.getActualMaximum(Calendar.DAY_OF_MONTH))
            return null;

        if(c2.get(Calendar.MONTH) != c1.get(Calendar.MONTH) ||
           c2.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) {

            return null;
        }

        return new Month(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH)+1);

    }
}
