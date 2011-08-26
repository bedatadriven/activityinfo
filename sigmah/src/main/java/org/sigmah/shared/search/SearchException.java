package org.sigmah.shared.search;

import org.sigmah.shared.exception.CommandException;

public class SearchException extends CommandException {

	public SearchException(String message) {
		super(message);
	}

	public SearchException(Throwable e) {
		super(e);
	}
	
}
