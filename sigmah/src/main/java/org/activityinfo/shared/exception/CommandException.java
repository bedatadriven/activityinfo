package org.activityinfo.shared.exception;

import org.activityinfo.shared.command.result.CommandResult;

public class CommandException extends Exception implements CommandResult {

	public CommandException(Exception ex) {
		super(ex);
	}

    public CommandException(String message) {
        super(message);
    }

    public CommandException() {
	}

}
