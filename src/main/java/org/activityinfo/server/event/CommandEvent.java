package org.activityinfo.server.event;

import org.activityinfo.server.endpoint.gwtrpc.ServerExecutionContext;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

@SuppressWarnings("rawtypes")
public class CommandEvent {
	private Command command;
	private CommandResult result;
	private ServerExecutionContext context;
	
	public CommandEvent(Command command, CommandResult result, ServerExecutionContext context) {
		this.command = command;
		this.result = result;
		this.context = context;
	}
	
	public Command getCommand() {
		return this.command;
	}
	
	public CommandResult getResult() {
		return result;
	}
	
	public ServerExecutionContext getContext() {
		return context;
	}
	
	@Override
	public String toString() {
		return "CommandEvent ["+getCommand().getClass().getSimpleName()+"]";
	}
}
