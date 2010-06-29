package org.sigmah.shared.exception;

public class InvalidAuthTokenException extends CommandException {

	public InvalidAuthTokenException() {
		
	}

	public InvalidAuthTokenException(String message) {
		super(message);
	}


}
