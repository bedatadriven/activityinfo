package org.activityinfo.client.command.loader;

import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.GetListCommand;
import org.activityinfo.shared.command.result.BatchResult;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.ListResult;


/**
 * This is a replacement for the BaseLoader shipped with GXT that fits in
 * with the command pattern.
 * 
 * Note that the loadConfig object is ignored.
 * 
 * @author Alex Bertram
 *
 * @param <ResultT>
 * @param <CommandT>
 */
public abstract class AbstractCmdLoader<	ResultT extends ListResult,
											CommandT extends GetListCommand<ResultT>
										>
        extends AbstractLoader<ResultT> {

    private int requestId = 0;
	private CommandService service;
	private boolean remoteSort = true;
	
	private AsyncMonitor monitor = null;
	
	private CommandT command;
	
	public AbstractCmdLoader(CommandService service) {
		this.service = service;
	}
	
	public void setCommand(CommandT command) {
		this.command = command;
	}

    public CommandT getCommand() {
        return command;
    }

    public void setMonitor(AsyncMonitor monitor) {
		this.monitor = monitor;
	}


    protected void prepareCommand(CommandT cmd) {
		
	}
	
	
	@Override
	public boolean load() {
		return load(null);
	}

	@Override
	public boolean load(final Object loadConfig) {

		return execute();
	}
	
	public void setAsyncMonitor(AsyncMonitor monitor) {
		this.monitor = monitor;
	}
	
	protected boolean execute() {

        if(command == null) {
			throw new IllegalStateException("The command is null. (Did you forget to call Loader.setCommand() ?");
		}

        requestId++;
        final int executingId = requestId;

		prepareCommand(command);

        CommandLoadEvent preEvent = new CommandLoadEvent(AbstractCmdLoader.this, command);
        if(!fireEvent(Loader.BeforeLoad, preEvent)) {
			return false;
		}

        Command cmdToExecute;
        if(preEvent.getBatch().getCommands().size() != 0) {
            preEvent.addCommandToBatch(command);
		    cmdToExecute = preEvent.getBatch();
        } else {
            cmdToExecute = command;
        }
		service.execute(cmdToExecute, monitor, new AsyncCallback<CommandResult>() {

			@Override
			public void onFailure(Throwable caught) {
				fireEvent(Loader.LoadException, new LoadEvent(AbstractCmdLoader.this, new DummyLoadConfig(command), caught));
			}

			@Override
			public void onSuccess(CommandResult result) {

                if(executingId == requestId) {

                   ResultT loadResult;
                    if(result instanceof BatchResult) {
                        loadResult = (ResultT) ((BatchResult) result).getLastResult();
                    } else {
                        loadResult = (ResultT) result;
                    }

                    fireEvent(Loader.Load, new LoadEvent(AbstractCmdLoader.this,
                            new DummyLoadConfig(command), loadResult));

                }
            }
			
		});
		
		return true;
	}

}
