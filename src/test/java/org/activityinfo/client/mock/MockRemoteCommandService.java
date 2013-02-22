package org.activityinfo.client.mock;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.RemoteCommandServiceAsync;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.exception.CommandException;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MockRemoteCommandService implements RemoteCommandServiceAsync {

    public Map<Class, Integer> commandCounts = new HashMap<Class, Integer>();

    public SchemaDTO schema;

    public MockRemoteCommandService() {

    }

    public MockRemoteCommandService(SchemaDTO schema) {
        this.schema = schema;
    }

    public int getCommandCount(Class clazz) {
        Integer count = commandCounts.get(clazz);
        if (count == null) {
            return 0;
        } else {
            return count;
        }
    }

    @Override
    public void execute(String authToken, List<Command> cmds,
        AsyncCallback<List<CommandResult>> callback) {

        List<CommandResult> results = new ArrayList<CommandResult>();

        for (Command cmd : cmds) {

            Integer count = commandCounts.get(cmd.getClass());
            commandCounts.put(cmd.getClass(), count == null ? 1 : count + 1);

            if (schema != null && cmd instanceof GetSchema) {
                results.add(schema);
            } else {
                results.add(mockExecute(cmd));
            }
        }
        callback.onSuccess(results);
    }

    protected CommandResult mockExecute(Command cmd) {
        return new CommandException();
    }
}
