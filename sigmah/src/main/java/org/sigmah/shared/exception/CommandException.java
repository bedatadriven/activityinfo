package org.sigmah.shared.exception;

import org.sigmah.shared.command.result.CommandResult;

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
