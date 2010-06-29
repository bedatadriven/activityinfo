package org.sigmah.shared.exception;

/*
 * @author Alex Bertram
 */
public class UnexpectedCommandException extends CommandException {


    public UnexpectedCommandException() {
    }

    public UnexpectedCommandException(String message) {
        super(message);
    }
}
