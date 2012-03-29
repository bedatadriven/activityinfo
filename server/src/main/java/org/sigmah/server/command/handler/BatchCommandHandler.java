/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.BatchCommand;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.handler.CommandHandlerAsync;
import org.sigmah.shared.command.handler.ExecutionContext;
import org.sigmah.shared.command.result.BatchResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;

import com.google.common.primitives.Primitives;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.BatchCommand
 */
public class BatchCommandHandler implements CommandHandlerAsync<BatchCommand, BatchResult> {

    private final Injector injector;

    @Inject
    public BatchCommandHandler(Injector injector) {
        this.injector = injector;
    }

    @Override
	public void execute(BatchCommand batch, ExecutionContext context,
			final AsyncCallback<BatchResult> callback) {
    	
    	final ArrayList<CommandResult> results = new ArrayList<CommandResult>();
    	for(Command command : batch.getCommands()) {
    		results.add(null);
    	}
    	final boolean finished[] = new boolean[batch.getCommands().size()];
    	
    	for(int i=0; i!=batch.getCommands().size();++i) {
    		final int commandIndex = i;
    		context.execute(batch.getCommands().get(i), new AsyncCallback<CommandResult>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
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

    private boolean all(boolean[] finished) {
    	for(int i=0;i!=finished.length;++i) {
    		if(!finished[i]) {
    			return false;
    		}
    	} 
    	return true;
    }
}
