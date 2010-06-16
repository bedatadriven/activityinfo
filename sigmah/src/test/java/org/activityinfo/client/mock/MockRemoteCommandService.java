package org.activityinfo.client.mock;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.RemoteCommandServiceAsync;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.exception.CommandException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if(count == null) {
            return 0;
        } else {
            return count;
        }
    }


    @Override
    public void execute(String authToken, List<Command> cmds, AsyncCallback<List<CommandResult>> callback) {

        List<CommandResult> results = new ArrayList<CommandResult>();

        for(Command cmd : cmds ) {

            Integer count = commandCounts.get(cmd.getClass());
            commandCounts.put(cmd.getClass(), count == null ? 1 : count + 1);

            if(schema!=null && cmd instanceof GetSchema) {
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
