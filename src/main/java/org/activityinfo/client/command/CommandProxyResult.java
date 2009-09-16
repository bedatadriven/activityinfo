package org.activityinfo.client.command;/*
 * @author Alex Bertram
 */

public class CommandProxyResult<T> {

    public final boolean couldExecute;
    public final T result;

    private static CommandProxyResult failed = new CommandProxyResult();

    private CommandProxyResult() {
        couldExecute = false;
        result = null;
    }

    public CommandProxyResult(T result) {
        this.couldExecute = true;
        this.result = result;
    }

    public static CommandProxyResult couldNotExecute() {
        return failed;
    }


}
