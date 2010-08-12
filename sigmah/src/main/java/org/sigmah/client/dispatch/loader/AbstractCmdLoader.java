/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.loader;

import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.GetListCommand;
import org.sigmah.shared.command.result.BatchResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ListResult;


/**
 * Base class for command-pattern-based implementations of GXT loader interfaces
 *
 * Note that the loadConfig object is ignored.
 * @deprecated Use standard GXT loader with custom {@link com.extjs.gxt.ui.client.data.DataProxy}
 * @author Alex Bertram
 * @param <ResultT>
 * @param <CommandT>
 */
abstract class AbstractCmdLoader<ResultT extends ListResult,
        CommandT extends GetListCommand<ResultT>
        >
        extends AbstractLoader<ResultT> {

    private int requestId = 0;
    private Dispatcher service;
    private boolean remoteSort = true;

    private AsyncMonitor monitor = null;

    private CommandT command;

    public AbstractCmdLoader(Dispatcher service) {
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

        if (command == null) {
            throw new IllegalStateException("The command is null. (Did you forget to call Loader.setCommand() ?");
        }

        requestId++;
        final int executingId = requestId;

        prepareCommand(command);

        CommandLoadEvent preEvent = new CommandLoadEvent(AbstractCmdLoader.this, command);
        if (!fireEvent(Loader.BeforeLoad, preEvent)) {
            return false;
        }

        Command cmdToExecute;
        if (preEvent.getBatch().getCommands().size() != 0) {
            preEvent.addCommandToBatch(command);
            cmdToExecute = preEvent.getBatch();
        } else {
            cmdToExecute = command;
        }
        service.execute(cmdToExecute, monitor, new AsyncCallback<CommandResult>() {

            @Override
            public void onFailure(Throwable caught) {
                fireEvent(Loader.LoadException, new LoadEvent(AbstractCmdLoader.this, new PagingGetCommandConfig(command), caught));
            }

            @Override
            public void onSuccess(CommandResult result) {

                if (executingId == requestId) {

                    ResultT loadResult;
                    if (result instanceof BatchResult) {
                        loadResult = (ResultT) ((BatchResult) result).getLastResult();
                    } else {
                        loadResult = (ResultT) result;
                    }

                    fireEvent(Loader.Load, new LoadEvent(AbstractCmdLoader.this,
                            new PagingGetCommandConfig(command), loadResult));

                }
            }

        });

        return true;
    }

}
