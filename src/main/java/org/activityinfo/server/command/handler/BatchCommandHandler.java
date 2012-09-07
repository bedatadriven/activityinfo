/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.command.result.BatchResult;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.primitives.Primitives;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.BatchCommand
 */
public class BatchCommandHandler implements CommandHandlerAsync<BatchCommand, BatchResult> {

    private static final Logger LOGGER = Logger.getLogger(BatchCommandHandler.class);
    

    @Override
	public void execute(BatchCommand batch, ExecutionContext context,
			final AsyncCallback<BatchResult> callback) {
    	
    	if(batch.getCommands().isEmpty()) {
    		LOGGER.warn("Received empty batch command");
    		callback.onSuccess(new BatchResult(Lists.<CommandResult>newArrayList()));
    	} else {
	    	final ArrayList<CommandResult> results = new ArrayList<CommandResult>();
	    	for(Command command : batch.getCommands()) {
	    		results.add(null);
	    	}
	    	final boolean finished[] = new boolean[batch.getCommands().size()];
	    	final List<Throwable> exceptions = Lists.newArrayList();
	    	
	    	for(int i=0; i!=batch.getCommands().size();++i) {
	    		final int commandIndex = i;
	    		context.execute(batch.getCommands().get(i), new AsyncCallback<CommandResult>() {
	
					@Override
					public void onFailure(Throwable caught) {
						if(exceptions.isEmpty()) {
							exceptions.add(caught);
							callback.onFailure(caught);
						}
					}
	
					@Override
					public void onSuccess(CommandResult result) {
						results.set(commandIndex, result);
						finished[commandIndex] = true;
						if(all(finished)) {
							callback.onSuccess(new BatchResult(results));
						}
					}
	    			
	    		});
	    		
	    	}
    	}
	}

    private boolean all(boolean[] finished) {
    	for(int i=0;i!=finished.length;++i) {
    		if(!finished[i]) {
    			return false;
    		}
    	} 
    	return true;
    }
}
