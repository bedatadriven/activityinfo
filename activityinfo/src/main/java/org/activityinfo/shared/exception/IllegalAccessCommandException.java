package org.activityinfo.shared.exception;

/**
 * @author Alex Bertram
 */
public class IllegalAccessCommandException extends UnexpectedCommandException {

    public IllegalAccessCommandException() {
    }

    public IllegalAccessCommandException(String message) {
        super(message);
    }
}
